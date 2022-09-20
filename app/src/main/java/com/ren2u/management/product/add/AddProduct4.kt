package com.ren2u.management.product.add

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.ren2u.R
import com.ren2u.databinding.ActivityAddProduct4Binding

class AddProduct4 : AppCompatActivity() {
    private var _binding: ActivityAddProduct4Binding?=null

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

    private fun getLatitude(): Double? {
        return intent.getDoubleExtra("latitude", 0.0)
    }

    private fun getLongitude(): Double? {
        return intent.getDoubleExtra("longitude", 0.0)
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
        setContentView(R.layout.activity_add_product4)

        _binding= ActivityAddProduct4Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        if(getLatitude()!=0.0){
            binding.locationEditButton.text="표시 완료"
        }

        binding.locationEditButton.setOnClickListener {
            val intent = Intent(this@AddProduct4, SetLocation::class.java)

            val id = getId()
            val filePath = getFilePath()
            val name = getName()
            val category = getCategory()
            val price = getPrice()
            val number = getNumber()
            val period = getPeriod()

            intent.apply {
                this.putExtra("id", id)
                this.putExtra("filePath", filePath) // 데이터 넣기
                this.putExtra("name", name)
                this.putExtra("category", category)
                this.putExtra("price", price)
                this.putExtra("number", number)
                this.putExtra("period", period)
            }

            startActivity(intent)
            finish()
            //onStop()
        }

        binding.nextButton.setOnClickListener {
            val detail=binding.detailEditText.text.toString()

            if(detail=="" || getLatitude()==0.0){
                showDialogFailed()
            }
            else {
                val intent = Intent(this@AddProduct4, AddProduct5::class.java)

                val id = getId()
                val filePath = getFilePath()
                val name = getName()
                val category = getCategory()
                val price = getPrice()
                val number = getNumber()
                val period = getPeriod()
                val latitude = getLatitude()
                val longitude = getLongitude()

                intent.apply {
                    this.putExtra("id", id)
                    this.putExtra("filePath", filePath) // 데이터 넣기
                    this.putExtra("name", name)
                    this.putExtra("category", category)
                    this.putExtra("price", price)
                    this.putExtra("number", number)
                    this.putExtra("period", period)
                    this.putExtra("detail", detail)
                    this.putExtra("latitude", latitude)
                    this.putExtra("longitude", longitude)
                }
                Log.d("test", latitude.toString() +","+ longitude.toString())

                startActivity(intent)
                finish()
            }
        }

        val view=binding.root
        setContentView(view)
    }

    private fun showDialogFailed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("오류")
            .setMessage("모든 항목을 작성해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->

                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    /*override fun onResume() {
        val location=MainActivity.GlobalApplication.prefs.getString("location", "xxxxxx")

        Log.d("test", location)
        super.onResume()
    }*/

}