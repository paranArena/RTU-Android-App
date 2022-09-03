package com.rtu.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.rtu.R
import com.rtu.databinding.ActivityManageBinding

class ManageActivity : AppCompatActivity() {
    private var _binding: ActivityManageBinding?=null

    private val binding get() = _binding!!

    private fun getExtra(): Int {
        return intent.getIntExtra("id", 0)
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
        setContentView(R.layout.activity_manage)

        _binding= ActivityManageBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val id=getExtra()

        binding.notices.setOnClickListener {
            val intent = Intent(this, ManageNotice::class.java)
            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }
            startActivity(intent)
        }

        binding.members.setOnClickListener {
            val intent = Intent(this, ManageMember::class.java)
            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }
            startActivity(intent)
        }

        binding.products.setOnClickListener {
            val intent = Intent(this, ManageProduct::class.java)
            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }
            startActivity(intent)
        }

        val view=binding.root
        setContentView(view)
    }
}