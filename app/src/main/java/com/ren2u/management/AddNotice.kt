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
import ren2u.R
import ren2u.databinding.ActivityAddNoticeBinding
import com.ren2u.model.CreateNoticeResponse
import com.ren2u.retrofit.RetrofitBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddNotice : AppCompatActivity() {

    private var _binding: ActivityAddNoticeBinding?=null

    private val binding get() = _binding!!

    private fun getExtra(): Int {
        return intent.getIntExtra("id", 0)
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notice)

        _binding= ActivityAddNoticeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) //permission check
            == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), // 1
                PERMISSION_REQUEST_CODE) // 2
            showDialog("Permission granted")
        }

        lateinit var filePath : String
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

                Log.d("test",filePath)

                inputStream!!.close()
                inputStream = null
                bitmap?.let {
                    binding.noticeImage.setImageBitmap(bitmap)

                } ?: let {
                    Log.d("kkang", "bitmap null")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.noticeImage.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.completeButton.setOnClickListener {
            val id=getExtra()
            if(filePath==null){
                showDialogToGetImg()
            }
            else {
                val inputTitle = binding.noticeNameEditText.text.toString().replace("'", """\'""")
                val inputContent = binding.noticeEditText.text.toString().replace("'", """\'""")

                val titleRequest = RequestBody.create(MediaType.parse("text/plain"), inputTitle);
                val contentRequest = RequestBody.create(MediaType.parse("text/plain"), inputContent);

                Log.d("test", inputTitle)


                var file = File(filePath)
                var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                var body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                RetrofitBuilder.api.createNoticeRequest(
                    titleRequest, contentRequest, body, id
                ).enqueue(object : Callback<CreateNoticeResponse> {
                    override fun onResponse(
                        call: Call<CreateNoticeResponse>,
                        response: Response<CreateNoticeResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddNotice, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                            /*val intent = Intent(this@AddNotice, ManageNotice::class.java)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))*/
                            finish()
                        } else {
                            when (response.code()) {
                                400 -> Log.d("test", response.body()!!.toString())
                            }
                            Log.d("test", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<CreateNoticeResponse>, t: Throwable) {
                        Toast.makeText(this@AddNotice, "실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d("test", "실패$t")
                        finish()
                    }
                })
            }

        }

        val view=binding.root
        setContentView(view)
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