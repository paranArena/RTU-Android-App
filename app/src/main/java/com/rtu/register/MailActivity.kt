package com.rtu.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.rtu.R
import com.rtu.databinding.ActivityMailBinding
import com.rtu.model.*
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MailActivity : AppCompatActivity() {
    private var _binding: ActivityMailBinding?=null

    private val binding get() = _binding!!

    private var timerTask: Timer? = null
    private var time=300

    private fun getEmail(): String? {
        return intent.getStringExtra("email")
    }

    private fun getPassword(): String? {
        return intent.getStringExtra("password")
    }

    private fun getName(): String? {
        return intent.getStringExtra("name")
    }

    private fun getMajor(): String? {
        return intent.getStringExtra("major")
    }

    private fun getPhoneNumber(): String? {
        return intent.getStringExtra("phoneNumber")
    }

    private fun getId(): String? {
        return intent.getStringExtra("id")
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
        setContentView(R.layout.activity_mail)

        _binding = ActivityMailBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.message.visibility= View.INVISIBLE

        timeStart()

        if(time<0){
            timePause()
        }

        val email=getEmail()

        binding.nextButton.setOnClickListener {
            val code=binding.pin.text.toString()
            val requestCode=MailCodeModel(email=email!!, code=code)

            checkCode(requestCode)

        }

        val view=binding.root
        setContentView(view)
    }

    private fun timeStart(){

        timerTask = kotlin.concurrent.timer(period = 1000) {	// timer() 호출
            time--	// period=10, 0.01초마다 time를 1씩 증가
            val sec = time % 60	// time/100, 나눗셈의 몫 (초 부분)
            val min = time / 60	// time%100, 나눗셈의 나머지 (밀리초 부분)

            // UI조작을 위한 메서드
            runOnUiThread {
                binding.sec.text = "$sec"	// TextView 세팅
                binding.min.text = "$min"	// Textview 세팅
            }
        }
    }

    private fun timePause(){
        timerTask?.cancel()
    }

    private fun success(){
        val email=getEmail()
        val password=getPassword()
        val name=getName()
        val major=getMajor()
        val phoneNumber=getPhoneNumber()
        val id=getId()

        val registerData = RegisterRequest(
            email = email!!, password = password!!, name = name!!, major = major!!,
            phoneNumber = phoneNumber!!, studentId = id!!
        )

        signUp(registerData)
    }

    private fun failed(){
        binding.message.visibility= View.VISIBLE
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
                        val intent = Intent(this@MailActivity, WelcomeActivity::class.java)

                        startActivity(intent)
                        finish()
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

    private fun checkCode(requestCode: MailCodeModel){
        RetrofitBuilder.api.getVerifyCode(requestCode).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    var data = response.body()!!

                    if(data.statusCode==200){
                        success() //인증성공
                    }
                }
                else {
                    Log.d("fail", response.body().toString())
                    failed() //회원가입 실패
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }
}