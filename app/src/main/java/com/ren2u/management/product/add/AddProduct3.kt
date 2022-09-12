package com.ren2u.management.product.add

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import ren2u.R
import ren2u.databinding.ActivityAddProduct3Binding

class AddProduct3 : AppCompatActivity() {
    private var _binding: ActivityAddProduct3Binding?=null

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
            val number=binding.numberEditText.text.toString()
            val period=binding.periodEditText.text.toString()

            if(number=="" || period==""){
                showDialogFailed()
            }
            else {
                val intent = Intent(this@AddProduct3, AddProduct4::class.java)

                val id = getId()
                val filePath = getFilePath()
                val name = getName()
                val category = getCategory()
                val price = getPrice()

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
}