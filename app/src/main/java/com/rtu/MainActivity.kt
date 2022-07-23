package com.rtu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rtu.component.LoginComponent
import com.rtu.databinding.ActivityMainBinding
import com.rtu.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    //private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //private val viewModel: MainViewModel by lazy{MainViewModel()}

    private lateinit var mBinding: ActivityMainBinding
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.lifecycleOwner = this
        mBinding.viewModel = model

        var login=findViewById<ImageButton>(R.id.loginButton)


        login.setOnClickListener {
            val email=findViewById<EditText>(R.id.email).toString()
            val password=findViewById<EditText>(R.id.password).toString()

            val initializeRequest= LoginComponent(
                email=email, password=password)

            setLogin(initializeRequest)

            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
            finish()
        }
        //setupButtons()
    }

    private fun setLogin(request: LoginComponent){
        model.loginResponse.observe(
            this,
        ){
            model.loginResponse.value?.let{
                it->model.postLoginRequest(request)
            }
        }
    }

    /*private fun setupButtons() {
        model.startHomeActivityClickEvent.observe(
            this
        ) {
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }
    }*/
}