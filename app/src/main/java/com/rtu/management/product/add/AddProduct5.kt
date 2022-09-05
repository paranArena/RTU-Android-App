package com.rtu.management.product.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.rtu.R
import com.rtu.databinding.ActivityAddProduct5Binding

class AddProduct5 : AppCompatActivity() {
    private var _binding: ActivityAddProduct5Binding?=null

    private val binding get() = _binding!!

    private fun getId(): Int {
        return intent.getIntExtra("id", 0)
    }

    private fun getFilePath(): String? {
        return intent.getStringExtra("filePath")
    }

    private fun getName(): String? {
        return intent.getStringExtra("name")
    }

    private fun getCategory(): String? {
        return intent.getStringExtra("category")
    }

    private fun getPrice(): String? {
        return intent.getStringExtra("price")
    }

    private fun getNumber(): String? {
        return intent.getStringExtra("number")
    }

    private fun getPeriod(): String? {
        return intent.getStringExtra("period")
    }

    private fun getDetail(): String?{
        return intent.getStringExtra("detail")
    }

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
        setContentView(R.layout.activity_add_product5)

        _binding= ActivityAddProduct5Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)



        binding.nextButton.setOnClickListener {
            val id = getId()
            val filePath = getFilePath()
            val name = getName()
            val category = getCategory()
            val price = getPrice()
            val number = getNumber()
            val period = getPeriod()
            val detail = getDetail()
            val caution=binding.cautionEditText.text

            Log.d("test", filePath!!)
        }

        val view=binding.root
        setContentView(view)
    }
}