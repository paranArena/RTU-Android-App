package com.ren2u

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.ren2u.model.LoginRequest
import com.ren2u.model.LoginResponse
import com.ren2u.register.MailActivity
import com.ren2u.retrofit.RetrofitBuilder
import com.ren2u.databinding.ActivityMainBinding
import com.ren2u.register.ResetPasswordActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding?=null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding= ActivityMainBinding.inflate(layoutInflater)

        val view=binding.root
        setContentView(view)

        //binding.loginButton.isEnabled=false

        val tokenCheck=GlobalApplication.prefs.getString("token","x")

        if(tokenCheck!="x"){
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

            val id=binding.email.text.toString()
            val pw=binding.password.text.toString()

            val loginData=LoginRequest(email=id, password = pw)

            login(loginData)
        }

        binding.passwordLost.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun success(){
        binding.wrongMessage.visibility= View.INVISIBLE
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }

    private fun failed(){
        binding.wrongMessage.visibility= View.VISIBLE
    }

    private fun login(loginRequest: LoginRequest){
        RetrofitBuilder.api.loginPostRequest(loginRequest).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    var data = response.body()!!

                    if(data.token!=null){
                        val token=data.token
                        GlobalApplication.prefs.setString("token", token!!)
                        success() //로그인 성공
                    }
                }
                else {
                    Log.d("fail", response.code().toString())

                    if(response.code()==403){
                        val email=loginRequest.email

                        val intent = Intent(this@MainActivity, MailActivity::class.java)

                        intent.apply {
                            this.putExtra("email", email)
                        }
                        startActivity(intent)

                    }

                    failed() //로그인 실패
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    class GlobalApplication : Application() {
        companion object{
            lateinit var prefs : PreferenceUtil
        }
        override fun onCreate() {
            prefs= PreferenceUtil(applicationContext)
            super.onCreate()
        }
    }

    class PreferenceUtil(context: Context)
    {
        private val prefs: SharedPreferences =
            context.getSharedPreferences("tokens", Context.MODE_PRIVATE)

        private val location: SharedPreferences =
            context.getSharedPreferences("location", Context.MODE_PRIVATE)

        fun getLocation(key: String, defValue: String): String
        {
            return prefs.getString(key, defValue).toString()
        }

        fun setLocation(key: String, str: String)
        {
            prefs.edit().putString(key, str).apply()
        }

        fun getString(key: String, defValue: String): String
        {
            return prefs.getString(key, defValue).toString()
        }

        fun setString(key: String, str: String)
        {
            prefs.edit().putString(key, str).apply()
        }

        fun removeString(key: String)
        {
            prefs.edit().remove(key).commit()
        }
    }
}