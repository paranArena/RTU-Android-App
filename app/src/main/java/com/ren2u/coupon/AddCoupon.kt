package com.ren2u.coupon

import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.ren2u.MainPageActivity
import com.ren2u.R
import com.ren2u.databinding.ActivityAddCouponBinding
import com.ren2u.model.BasicResponse
import com.ren2u.model.CouponModel
import com.ren2u.model.CreateClubResponse
import com.ren2u.model.ImageResponse
import com.ren2u.retrofit.RetrofitBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AddCoupon : AppCompatActivity() {

    private var _binding: ActivityAddCouponBinding? = null

    private val binding get() = _binding!!

    private var imagePath: String?=null

    private var mLatitude: Double=37.283672
    private var mLongitude: Double=127.045295

    private fun getExtra(): Int {
        return intent.getIntExtra("id", 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coupon)

        _binding = ActivityAddCouponBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.locationEditButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame, CouponLocationFragment())
                .addToBackStack(null) // replace 다음에 적어준다.
                .commit()
        }

        val clubId=getExtra()

        var filePath : String?=null
        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            try {
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )

                val resolver: ContentResolver
                resolver = this.contentResolver

                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = resolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)

                filePath=getRealPathFromURI(it.data!!.data!!)
                uploadImage(filePath!!)

                Log.d("test",filePath!!)

                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.couponImage.setImageBitmap(bitmap)

                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.couponImage.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.completeButton.setOnClickListener {
            if(imagePath==null){
                showDialogToGetImg()
            }
            else{
                createCoupon(clubId)
            }
        }

        val view = binding.root
        setContentView(view)
    }

    private fun createCoupon(clubId: Int){
        val name=binding.groupNameEditText.text.toString()
        val locationName=binding.locationEditText.text.toString()
        val information=binding.introEditText.text.toString()

        var year=binding.actPicker.year.toString()
        var month=(binding.actPicker.month+1).toString().padStart(2, '0')
        var day=binding.actPicker.dayOfMonth.toString().padStart(2, '0')

        val actDate=year + "-" + month + "-" + day + "T00:00:00.00000"

        year=binding.expPicker.year.toString()
        month=(binding.expPicker.month+1).toString().padStart(2, '0')
        day=binding.expPicker.dayOfMonth.toString().padStart(2, '0')

        val expDate=year + "-" + month + "-" + day + "T23:59:59.00000"

        val compTime = actDate.substring(0..22)
        val compTime2 = expDate.substring(0..22)
        val now = LocalDateTime.now() // 현재 시간
        val convertTime = LocalDateTime.parse(compTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val convertTime2 = LocalDateTime.parse(compTime2, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val compareTime = ChronoUnit.SECONDS.between(now, convertTime)
        val compareTime2 = ChronoUnit.SECONDS.between(convertTime, convertTime2)

        if(compareTime<0 || compareTime2<0){
            showDialogTime()
        } else {

            val request = CouponModel(
                name = name,
                locationName = locationName,
                information = information,
                imagePath = imagePath!!,
                actDate = actDate,
                expDate = expDate,
                longitude = mLatitude,
                latitude = mLongitude
            )

            Log.d("test", request.toString())

            RetrofitBuilder.api.createCouponAdmin(
                clubId, request
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddCoupon, "등록 되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        when (response.code()) {
                            400 -> Log.d("test", response.body()!!.toString())
                        }
                        Log.d("test", response.code()!!.toString())
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Toast.makeText(this@AddCoupon, "실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("test", "실패$t")
                    finish()
                }
            })
        }
    }

    private fun uploadImage(filePath: String){
        var file = File(filePath)
        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        RetrofitBuilder.api.upload(
            body
        ).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(
                call: Call<ImageResponse>,
                response: Response<ImageResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddCoupon, "사진 업로드 완료", Toast.LENGTH_SHORT).show()
                    imagePath=response.body()!!.data
                } else {
                    when (response.code()) {
                        400 -> Log.d("test", response.body()!!.toString())
                    }
                    Log.d("test", response.body()!!.toString())
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Toast.makeText(this@AddCoupon, "실패했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("test", "실패$t")
                finish()
            }
        })
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val resolver: ContentResolver
        resolver = this.contentResolver

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = resolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 3
            }
        }
        return inSampleSize
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var buildName = Build.MANUFACTURER
        if (buildName.equals("Xiaomi")) {
            return uri.path!!
        }
        var columnIndex = 0
        var proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }

    private fun showDialogToGetImg(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("업로드 실패")
            .setMessage("사진을 등록해 주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->

                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    private fun showDialogTime(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("업로드 실패")
            .setMessage("올바르지 않은 날짜입니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->

                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun setLocation(latitude: Double, longitude: Double){
        binding.locationEditButton.text="설정됨"
        mLatitude=latitude
        mLongitude=longitude
    }
}