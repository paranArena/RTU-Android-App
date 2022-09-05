package com.rtu.management.product.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.rtu.R
import com.rtu.databinding.ActivitySetLocationBinding
class SetLocation : AppCompatActivity(){
    private var _binding: ActivitySetLocationBinding?=null

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
        setContentView(R.layout.activity_set_location)

        _binding= ActivitySetLocationBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportFragmentManager.beginTransaction().add(R.id.frame, MapsFragment()).commit()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.nextButton.setOnClickListener {
            finish()
        }

        val view=binding.root
        setContentView(view)
    }
}