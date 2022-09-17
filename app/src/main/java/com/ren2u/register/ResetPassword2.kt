package com.ren2u.register

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ren2u.R
import com.ren2u.databinding.ActivityResetPassword2Binding
import com.ren2u.databinding.ActivityResetPasswordBinding
import com.ren2u.model.BasicResponse
import com.ren2u.model.MailCodeModel
import com.ren2u.model.MailModel
import com.ren2u.model.VerifyModel
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ResetPassword2 : AppCompatActivity() {
    private var _binding: ActivityResetPassword2Binding? = null

    private val binding get() = _binding!!

    private var timerTask: Timer? = null
    private var time=300

    private fun getEmail(): String? {
        return intent.getStringExtra("email")
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
        setContentView(R.layout.activity_reset_password2)

        _binding = ActivityResetPassword2Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        timeStart()

        binding.nextButton.setOnClickListener {
            val email=getEmail()
            val code=binding.pin.text.toString()
            val password=binding.passwordEdit.text.toString()
            val passwordCheck=binding.passwordCheckEdit.text.toString()

            if(password.length<8){
                failed("length")
            } else if(password!=passwordCheck){
                failed("check")
            } else{
                val request=VerifyModel(email=email!!, code=code, password=password)
                checkCode(request)
            }
        }

        val view = binding.root
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

    private fun checkCode(requestCode: VerifyModel){
        RetrofitBuilder.api.passwordResetWithVerificationCode(requestCode).enqueue(object :
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
                    failed("code") //회원가입 실패
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    private fun success(){
        finish()
    }

    private fun failed(s: String){
        if(s=="code") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("인증 실패")
                .setMessage("인증 코드를 확인해 주세요")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }

        if(s=="length") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("비밀번호 오류")
                .setMessage("비밀번호는 8자 이상이여야 합니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }

        if(s=="check") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("비밀번호 오류")
                .setMessage("비밀번호가 일치하지 않습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }

}