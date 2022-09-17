package com.ren2u.register

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ren2u.R
import com.ren2u.databinding.ActivityResetPasswordBinding
import com.ren2u.model.BasicResponse
import com.ren2u.model.MailModel
import com.ren2u.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ResetPasswordActivity : AppCompatActivity() {
    private var _binding: ActivityResetPasswordBinding? = null

    private val binding get() = _binding!!


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
        setContentView(R.layout.activity_reset_password)

        _binding = ActivityResetPasswordBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.requestButton.setOnClickListener {
            val email=binding.email.text.toString()
            var request=MailModel(email=email)
            requestCode(request)
        }

        val view = binding.root
        setContentView(view)
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

                        success()
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

    private fun success(){
        val intent = Intent(this@ResetPasswordActivity, ResetPassword2::class.java)

        val email=binding.email.text.toString()
        intent.apply {
            this.putExtra("email", email)
        }
        startActivity(intent)
        finish()
    }

    private fun failed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("인증코드 전송 실패")
            .setMessage("이메일을 확인해 주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->

                })
        // 다이얼로그를 띄워주기
        builder.show()
    }
}