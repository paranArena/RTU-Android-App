package com.ren2u.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import ren2u.R
import ren2u.databinding.ActivityMailBinding
import com.ren2u.model.*
import com.ren2u.retrofit.RetrofitBuilder
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

        val email=getEmail()
        val requestMail = MailModel(email=email!!)

        requestCode(requestMail)

        timeStart()

        if(time<0){
            timePause()
        }

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
                binding.min.text = "$min : "	// Textview 세팅
            }
        }
    }

    private fun timePause(){
        timerTask?.cancel()
    }

    private fun success(){
        val intent = Intent(this@MailActivity, WelcomeActivity::class.java)

        startActivity(intent)
        finish()
    }

    private fun failed(){
        binding.message.visibility= View.VISIBLE
    }


    private fun requestCode(requestMail: MailModel){
        RetrofitBuilder.api.getRequestCode(requestMail).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    var data = response.body()!!

                    if(data.statusCode==200){
                        Toast.makeText(applicationContext, "인증번호가 전송되었습니다.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Log.d("fail", response.body().toString())
                    failed() //회원가입 실패
                    //success()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
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