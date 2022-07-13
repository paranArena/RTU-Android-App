package com.rtu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.rtu.databinding.ActivityMainBinding
import com.rtu.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    //private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //private val viewModel: MainViewModel by lazy{MainViewModel()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ViewDataBinding? = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        //val intent = Intent(this, RegisterActivity::class.java)
        //startActivity(intent)

    }
}