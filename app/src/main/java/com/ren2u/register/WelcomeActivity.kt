package com.ren2u.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ren2u.R
import com.ren2u.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private var _binding: ActivityWelcomeBinding?=null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        _binding= ActivityWelcomeBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.nextButton.setOnClickListener {
            finish()
        }

        val view=binding.root
        setContentView(view)
    }
}