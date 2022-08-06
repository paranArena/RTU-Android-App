package com.rtu


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.rtu.databinding.ActivityRegisterBinding
import com.rtu.model.RegisterRequest
import com.rtu.model.RegisterResponse
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding?=null

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
        setContentView(R.layout.activity_register)

        _binding= ActivityRegisterBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val view=binding.root
        setContentView(view)


        binding.nextButton.setOnClickListener {
            val email=buildString{
                append(binding.mailEditText.text.toString())
                append("@ajou.ac.kr")
            }
            val password=binding.passwordEditText.text.toString()
            val name=binding.nameEditText.text.toString()
            val major=binding.majorEditText.text.toString()
            val phoneNumber=binding.numberEditText.text.toString()
            val id=binding.idEditText.text.toString()


            val registerData= RegisterRequest(
                email=email, password=password, name=name, major=major,
                phoneNumber = phoneNumber, studentId = id
            )

            signUp(registerData)

        }
    }

    private fun success(){
        finish()
    }

    private fun failed(){

    }

    private fun signUp(registerRequest: RegisterRequest){
        RetrofitBuilder.api.registerPostRequest(registerRequest).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    var data = response.body()!!

                    if(data.statusCode==200){
                        success() //회원가입 성공
                    }
                }
                else {
                    Log.d("fail", response.body().toString())
                    failed() //회원가입 실패
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }
}