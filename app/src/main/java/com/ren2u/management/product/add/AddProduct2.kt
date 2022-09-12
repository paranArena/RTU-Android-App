package com.ren2u.management.product.add

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import ren2u.R
import ren2u.databinding.ActivityAddProduct2Binding

class AddProduct2 : AppCompatActivity() {
    private var _binding: ActivityAddProduct2Binding?=null

    private val binding get() = _binding!!

    private fun getId(): Int {
        return intent.getIntExtra("id", 0)
    }

    private fun getFilePath(): String? {
        return intent.getStringExtra("filePath")
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
        setContentView(R.layout.activity_add_product2)

        _binding= ActivityAddProduct2Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.categoryEditButton.setOnClickListener {
            selectCategory()
        }

        binding.nextButton.setOnClickListener {

            val name=binding.nameEditText.text.toString()
            val category=binding.categoryEditButton.text.toString()
            val price=binding.priceEditText.text.toString()

            if(name=="" || category=="" || price==""){
                showDialogFailed()
            }
            else {
                val intent = Intent(this@AddProduct2, AddProduct3::class.java)

                val id = getId()
                val filePath = getFilePath()

                intent.apply {
                    this.putExtra("id", id)
                    this.putExtra("filePath", filePath) // 데이터 넣기
                    this.putExtra("name", name)
                    this.putExtra("category", category)
                    this.putExtra("price", price)
                }
                startActivity(intent)
                finish()
            }
        }

        val view=binding.root
        setContentView(view)
    }

    private fun selectCategory(){
        val items = arrayOf("가전제품", "게임/취미", "공부할 때", "도서",
        "디지털기기", "비오는 날", "생활용품", "스포츠/레저", "의류", "주방용 기구", "춥거나, 더울 때",
        "피크닉", "행사", "기타")
        val builder = AlertDialog.Builder(this)
            .setTitle("카테고리")
            .setItems(items) { dialog, which ->
                binding.categoryEditButton.text="${items[which]}"
            }
            .show()
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