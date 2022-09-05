package com.rtu.management.product.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.rtu.R
import com.rtu.databinding.ActivityAddProduct3Binding

class AddProduct3 : AppCompatActivity() {
    private var _binding: ActivityAddProduct3Binding?=null

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
        setContentView(R.layout.activity_add_product3)

        _binding= ActivityAddProduct3Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        binding.nextButton.setOnClickListener {
            val intent = Intent(this@AddProduct3, AddProduct4::class.java)

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