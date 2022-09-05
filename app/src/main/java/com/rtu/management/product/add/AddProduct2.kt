package com.rtu.management.product.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.rtu.R
import com.rtu.databinding.ActivityAddProduct2Binding

class AddProduct2 : AppCompatActivity() {
    private var _binding: ActivityAddProduct2Binding?=null

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
        setContentView(R.layout.activity_add_product2)

        _binding= ActivityAddProduct2Binding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.categoryEditButton.setOnClickListener {
            selectCategory()
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this@AddProduct2, AddProduct3::class.java)

            intent.apply {
                //this.putExtra("filePath",filePath) // 데이터 넣기
            }
            startActivity(intent)
            finish()
        }

        val view=binding.root
        setContentView(view)
    }

    private fun selectCategory(){
        val items = arrayOf("가전제품", "게임/취미", "공부할 때", "도서",
        "디지털 기기", "비오는 날", "생활용품", "스포츠/레저", "의류", "주방용 기구", "춥거나, 더울 때",
        "피크닉", "행사", "기타")
        val builder = AlertDialog.Builder(this)
            .setTitle("카테고리")
            .setItems(items) { dialog, which ->
                binding.categoryEditButton.text="${items[which]}"
            }
            .show()
    }
}