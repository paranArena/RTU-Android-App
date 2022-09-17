package com.ren2u.mypagetab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.ren2u.R
import com.ren2u.databinding.ActivityMyInfoBinding
import com.ren2u.model.MyInfoModel
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoActivity : AppCompatActivity() {
    private var _binding: ActivityMyInfoBinding?=null

    private val binding get() = _binding!!

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
        setContentView(R.layout.activity_my_info)

        _binding = ActivityMyInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.name.isEnabled = false
        binding.major.isEnabled =false
        binding.studentId.isEnabled=false
        binding.email.isEnabled=false
        binding.number.isEnabled=false

        getMyInfo()

        val view = binding.root
        setContentView(view)
    }

    private fun getMyInfo(){
        RetrofitBuilder.api.myInfoRequest().enqueue(object :
            Callback<MyInfoModel> {
            override fun onResponse(
                call: Call<MyInfoModel>,
                response: Response<MyInfoModel>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    val data=response.body()

                    val name=data!!.data.name
                    val major=data!!.data.major
                    val studentId=data!!.data.studentId
                    val email=data!!.data.email
                    val phoneNumber=data!!.data.phoneNumber

                    binding.name.setText(name)
                    binding.major.setText(major)
                    binding.studentId.setText(studentId)
                    binding.email.setText(email)
                    binding.number.setText(phoneNumber)

                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyInfoModel>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }
}