package com.rtu


import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.rtu.databinding.ActivityRegisterBinding
import com.rtu.model.RegisterRequest
import com.rtu.model.RegisterResponse
import com.rtu.register.MailActivity
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding?=null

    private val binding get() = _binding!!
    private var mailCheck=false

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

        initVisible()

        binding.button1.setOnClickListener {
            if(binding.passwordEditText.inputType==81) binding.passwordEditText.inputType=1
            else binding.passwordEditText.inputType=81


        }

        binding.button2.setOnClickListener {
            if(binding.passwordCheckEditText.inputType==81) binding.passwordCheckEditText.inputType=1
            else binding.passwordCheckEditText.inputType=81
        }


        binding.mailButton.setOnClickListener {
            val temp=binding.mailEditText.text.toString()
            if(temp.length<=5){
                binding.mailHelp.visibility= View.VISIBLE
            }
            else {
                val email = buildString {
                    append(binding.mailEditText.text.toString())
                    append("@ajou.ac.kr")
                }

                checkEmail(email)
            }
        }


        binding.nextButton.setOnClickListener {
            val email=buildString{
                append(binding.mailEditText.text.toString())
                append("@ajou.ac.kr")
            }
            val password=binding.passwordEditText.text.toString()
            val passwordCheck=binding.passwordCheckEditText.text.toString()
            val name=binding.nameEditText.text.toString()
            val major=binding.majorEditText.text.toString()
            val phoneNumber=binding.numberEditText.text.toString()
            val id=binding.idEditText.text.toString()

            initVisible()

            if(!mailCheck){
                binding.mailHelp.text="이메일 중복확인을 해주세요"
                binding.mailEditText.callOnClick()
                binding.mailHelp.visibility=View.VISIBLE
                failed()
            } else if(password.length<8){
                binding.passwordHelp.visibility=View.VISIBLE
                failed()
            } else if(password!=passwordCheck){
                binding.passwordCheckHelp.visibility=View.VISIBLE
                failed()
            } else if(name.length<2){
                binding.nameHelp.visibility=View.VISIBLE
                failed()
            } else if(id.length<6){
                binding.idHelp.visibility=View.VISIBLE
                failed()
            } else {

                val registerData = RegisterRequest(
                    email = email!!, password = password!!, name = name!!, major = major!!,
                    phoneNumber = phoneNumber!!, studentId = id!!
                )

                signUp(registerData)
            }

        }

        val view=binding.root
        setContentView(view)
    }

    private fun initVisible(){
        binding.idHelp.visibility= View.INVISIBLE
        binding.mailHelp.visibility= View.INVISIBLE
        binding.majorHelp.visibility= View.INVISIBLE
        binding.nameHelp.visibility= View.INVISIBLE
        binding.numberHelp.visibility= View.INVISIBLE
        binding.passwordCheckHelp.visibility= View.INVISIBLE
        binding.passwordHelp.visibility= View.INVISIBLE
    }

    private fun success(){

        val email=buildString{
            append(binding.mailEditText.text.toString())
            append("@ajou.ac.kr")
        }

        val intent = Intent(this@RegisterActivity, MailActivity::class.java)

        intent.apply {
            this.putExtra("email", email)
        }
        startActivity(intent)
        finish()
    }

    private fun failed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원가입에 실패")
            .setMessage("다시 한 번 확인 후 진행해 주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->

                })
        // 다이얼로그를 띄워주기
        builder.show()
    }


    private fun checkEmail(email: String){
        RetrofitBuilder.api.checkEmailRequest(email).enqueue(object :
            Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    var data = response.body()!!

                    if(data){
                        binding.mailHelp.text="이미 가입된 이메일 입니다."
                        binding.mailHelp.setTextColor(ContextCompat.getColor(
                            applicationContext!!, R.color.red
                        ))
                        binding.mailHelp.visibility=View.VISIBLE
                    }
                    else{
                        binding.mailHelp.text="사용 가능한 이메일 입니다."
                        binding.mailHelp.setTextColor(ContextCompat.getColor(
                            applicationContext!!, R.color.green
                        ))
                        binding.mailHelp.visibility=View.VISIBLE
                        mailCheck=true
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
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