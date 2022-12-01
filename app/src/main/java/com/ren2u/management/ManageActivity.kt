package com.ren2u.management

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.ren2u.R
import com.ren2u.databinding.ActivityManageBinding
import com.ren2u.management.coupon.ManageCouponActivity
import com.ren2u.model.BasicResponse
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.toolbarOption.setOnClickListener {
            showDialog()
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, UpdateClubActivity::class.java)
            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }
            startActivity(intent)
        }

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

        binding.coupon.setOnClickListener {
            val intent = Intent(this, ManageCouponActivity::class.java)
            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }
            startActivity(intent)
        }

        val view=binding.root
        setContentView(view)
    }

    fun showDialog(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("그룹 삭제")
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        deleteClub(getExtra())
                    }).setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                })
            // 다이얼로그를 띄워주기
            builder.show()
    }

    private fun deleteClub(id: Int){
        RetrofitBuilder.api.deleteClub(id).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(

                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    finish()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}