package com.ren2u.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.ren2u.R
import com.ren2u.databinding.ActivityMailBinding
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

    private fun getPassword(): String?{
        return intent.getStringExtra("password")
    }

    private fun getName(): String?{
        return intent.getStringExtra("name")
    }

    private fun getPhoneNumber(): String?{
        return intent.getStringExtra("phoneNumber")
    }

    private fun getStudentId(): String?{
        return intent.getStringExtra("studentId")
    }

    private fun getMajor(): String?{
        return intent.getStringExtra("major")
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

       // requestCode(requestMail)

        timeStart()

        if(time<0){
            timePause()
        }

        binding.nextButton.setOnClickListener {
            val code=binding.pin.text.toString()
            val password=getPassword()
            val name=getName()
            val major=getMajor()
            val phoneNumber=getPhoneNumber()
            val studentId=getStudentId()


            val registerData = RegisterRequest(
                email = email!!, password = password!!, name = name!!, major = major!!,
                phoneNumber = phoneNumber!!, studentId = studentId!!, verificationCode = code
            )

            Log.d("test", registerData.toString())

            signUp(registerData)

        }


        binding.again.setOnClickListener {
            val request=MailModel(email=email)

            requestCode(request)
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
                        time=300
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
                        success()
                    }
                }
                else {
                    Log.d("fail", response.code().toString())
                    Log.d("fail", registerRequest.toString())
                    failed() //회원가입 실패
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }
}