package com.rtu.management.product.add


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.rtu.MainPageActivity
import com.rtu.R
import com.rtu.databinding.ActivityAddProduct5Binding
import com.rtu.model.CreateClubResponse
import com.rtu.model.CreateProductResponse
import com.rtu.retrofit.RetrofitBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProduct5 : AppCompatActivity() {
    private var _binding: ActivityAddProduct5Binding?=null

    private val binding get() = _binding!!

    private fun getId(): Int {
        return intent.getIntExtra("id", 0)
    }

    private fun getFilePath(): String? {
        return intent.getStringExtra("filePath")
    }

    private fun getName(): String? {
        return intent.getStringExtra("name")
    }

    private fun getCategory(): String? {
        return intent.getStringExtra("category")
    }

    private fun getPrice(): String? {
        return intent.getStringExtra("price")
    }

    private fun getNumber(): String? {
        return intent.getStringExtra("number")
    }

    private fun getPeriod(): String? {
        return intent.getStringExtra("period")
    }

    private fun getDetail(): String?{
        return intent.getStringExtra("detail")
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
        setContentView(R.layout.activity_add_product5)

        _binding= ActivityAddProduct5Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)



        binding.nextButton.setOnClickListener {
            val id = getId()
            val filePath = getFilePath()
            val name = getName()
            val category = getCategory()
            val price = getPrice()
            val number = getNumber()
            val period = getPeriod()
            val detail = getDetail()
            val caution=binding.cautionEditText.text.toString()

            var rentalList=mutableListOf<MultipartBody.Part>()

            if (number != null) {
                for(i in 1..number.toInt()){
                    rentalList.add(MultipartBody.Part.createFormData("rentalPolicies","FIFO"))
                }
            }

            val nameRequest = RequestBody.create(MediaType.parse("text/plain"), name)
            val categoryRequest = RequestBody.create(MediaType.parse("text/plain"), category)
            val priceRequest = RequestBody.create(MediaType.parse("text/plain"), price)
            val fifoRequest = RequestBody.create(MediaType.parse("text/plain"), period)
            val reserveRequest = RequestBody.create(MediaType.parse("text/plain"), "0")
            val locationRequest = RequestBody.create(MediaType.parse("text/plain"), detail)
            val longitudeRequest = RequestBody.create(MediaType.parse("text/plain"), "127.045295")
            val latitudeRequest = RequestBody.create(MediaType.parse("text/plain"), "37.283672")
            val cautionRequest = RequestBody.create(MediaType.parse("text/plain"), caution)

            var file = File(filePath)
            var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            RetrofitBuilder.api.createProductRequest(
                nameRequest, categoryRequest, priceRequest, rentalList!!,
                fifoRequest,reserveRequest,locationRequest,latitudeRequest,
                longitudeRequest,cautionRequest,body, id
            ).enqueue(object : Callback<CreateProductResponse> {
                override fun onResponse(
                    call: Call<CreateProductResponse>,
                    response: Response<CreateProductResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddProduct5, "등록되었습니다.", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        when (response.code()) {
                            400 -> Log.d("test", response.body()!!.toString())
                        }
                        Log.d("test", response.code()!!.toString())
                    }
                }

                override fun onFailure(call: Call<CreateProductResponse>, t: Throwable) {
                    Toast.makeText(this@AddProduct5, "실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d("test", "실패$t")
                    finish()
                }
            })
            Log.d("test", filePath!!)
        }

        val view=binding.root
        setContentView(view)
    }
}