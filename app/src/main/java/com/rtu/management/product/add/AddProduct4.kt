package com.rtu.management.product.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.rtu.R
import com.rtu.databinding.ActivityAddProduct4Binding

class AddProduct4 : AppCompatActivity() {
    private var _binding: ActivityAddProduct4Binding?=null

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
        setContentView(R.layout.activity_add_product4)

        _binding= ActivityAddProduct4Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.locationEditButton.setOnClickListener {
            val intent = Intent(this@AddProduct4, SetLocation::class.java)
            startActivity(intent)
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this@AddProduct4, AddProduct3::class.java)

            intent.apply {
                //this.putExtra("filePath",filePath) // 데이터 넣기
            }
            startActivity(intent)
            finish()
        }

        val view=binding.root
        setContentView(view)
    }
}