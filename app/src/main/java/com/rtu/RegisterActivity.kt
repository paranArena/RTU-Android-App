package com.rtu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.view.MenuItem
import com.rtu.databinding.ActivityRegisterBinding

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
        supportActionBar!!.setTitle("회원가입")

        val view=binding.root
        setContentView(view)

        /*binding.passwordCheckEditText.addOnEditTextAttachedListener {
            val previous=binding.passwordEditText.toString()

            if(previous!=binding.passwordCheckEditText.toString()){
                binding.passwordCheckEditText.error="비밀번호가 일치하지 않습니다."
            }
            else{
                binding.passwordCheckEditText.helperText="비밀번호가 일치합니다."
            }
        }*/

        binding.nextButton.setOnClickListener {

        }

    }
}