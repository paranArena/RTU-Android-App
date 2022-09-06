package com.rtu.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rtu.R
import com.rtu.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private var _binding: ActivityWelcomeBinding?=null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.nextButton.setOnClickListener {
            finish()
        }

        val view=binding.root
        setContentView(view)
    }
}