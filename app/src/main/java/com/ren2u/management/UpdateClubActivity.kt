package com.ren2u.management

import android.Manifest
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.databinding.ActivityUpdateClubBinding
import com.ren2u.model.*
import com.ren2u.retrofit.RetrofitBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdateClubActivity : AppCompatActivity() {

    private var _binding: ActivityUpdateClubBinding?=null

    private val binding get() = _binding!!

    private var upImagePath: String?=null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getClubExtra(): Int {
        return intent.getIntExtra("id", 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_club)

        _binding= ActivityUpdateClubBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) //permission check
            == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), // 1
                AddNotice.PERMISSION_REQUEST_CODE
            ) // 2
            showDialog("Permission granted")
        }

        val clubId=getClubExtra()

        getClubInfo(clubId)

        var filePath : String? =null
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
                    binding.groupImage.setImageBitmap(bitmap)

                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.groupImage.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.completeButton.setOnClickListener {
            if(upImagePath==null){
                showDialogToGetImg()
            }
            else {
                val inputName = binding.groupNameEditText.text.toString().replace("'", """\'""")
                val inputTag = binding.tagEditText.text.toString().replace("#", "").split(" ")
                val inputIntro = binding.introEditText.text.toString().replace("'", """\'""")

                val imagePaths=listOf(upImagePath)

                val request= CreateClubModel(
                    name=inputName,
                    introduction = inputIntro,
                    thumbnailPath = imagePaths as List<String>,
                    hashtags = inputTag
                )

                RetrofitBuilder.api.updateClubInfo(
                   clubId, request
                ).enqueue(object : Callback<CreateClubResponse> {
                    override fun onResponse(
                        call: Call<CreateClubResponse>,
                        response: Response<CreateClubResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@UpdateClubActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                            /*val intent = Intent(this@AddNotice, ManageNotice::class.java)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))*/
                            finish()
                        } else {
                            when (response.code()) {
                                400 -> Log.d("test", response.code()!!.toString())
                            }
                            Log.d("test", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<CreateClubResponse>, t: Throwable) {
                        Toast.makeText(this@UpdateClubActivity, "실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("test", "실패$t")
                        finish()
                    }
                })
            }

        }

        val view=binding.root
        setContentView(view)
    }

    private fun getClubInfo(clubId: Int){
        RetrofitBuilder.api.getGroupDetail(clubId).enqueue(object :
            Callback<ClubDetail> {
            override fun onResponse(

                call: Call<ClubDetail>,
                response: Response<ClubDetail>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val title = data.data.name
                    val introduction = data.data.introduction
                    val thumbnailPath = data.data.thumbnailPath

                    Glide.with(this@UpdateClubActivity).load(thumbnailPath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.groupImage)

                    upImagePath=thumbnailPath
                    var hashtags: String=""
                    for(tag in data.data.hashtags){
                        hashtags += "#"
                        hashtags += tag
                        hashtags += " "
                    }

                    binding.tagEditText.setText(hashtags)
                    binding.groupNameEditText.setText(title)
                    binding.introEditText.setText(introduction)
                }
            }

            override fun onFailure(call: Call<ClubDetail>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
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
                    Toast.makeText(this@UpdateClubActivity, "사진 업로드 완료", Toast.LENGTH_SHORT).show()
                    upImagePath=response.body()!!.data
                } else {
                    when (response.code()) {
                        400 -> Log.d("test", response.body()!!.toString())
                    }
                    Log.d("test", response.body()!!.toString())
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Toast.makeText(this@UpdateClubActivity, "실패했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("test", "실패$t")
                finish()
            }
        })
    }


    private fun showDialog(s: String) {
        if(s == "Permission granted"){
            showDialogToGetPermission()
        }
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
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

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("권한 요청")
            .setMessage("사진을 업로드하기 위해 권한이 필요합니다." +
                    "설정에서 파일에 접근 요청을 허용해 주세요.")

        builder.setPositiveButton("네") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)   // 6
        }
        builder.setNegativeButton("나중에") { dialogInterface, i ->
            // ignore
        }
        val dialog = builder.create()
        dialog.show()
    }
}