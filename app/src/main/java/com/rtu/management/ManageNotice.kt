package com.rtu.management

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.rtu.R
import com.rtu.adapter.MyNoticeViewAdapter
import com.rtu.databinding.ActivityManageNoticeBinding

import com.rtu.grouptap.NoticeInfo
import com.rtu.model.MyNotice
import com.rtu.model.NoticeModel
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageNotice : AppCompatActivity() {
    private var _binding: ActivityManageNoticeBinding?=null

    private val binding get() = _binding!!

    val data_ = mutableListOf<NoticeModel>()

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
        setContentView(R.layout.activity_manage_notice)

        _binding= ActivityManageNoticeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val id=getExtra()
        getNotice(id)

        binding.addNotice.setOnClickListener {
            val intent = Intent(this, AddNotice::class.java)

            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }

            startActivity(intent)
            onStop()
        }

        val view=binding.root
        setContentView(view)
    }

    override fun onResume(){
        val id=getExtra()
        getNotice(id)
        super.onResume()
    }

    private fun getNotice(id: Int){
        RetrofitBuilder.api.getClubNotice(id).enqueue(object :
            Callback<MyNotice> {
            override fun onResponse(

                call: Call<MyNotice>,
                response: Response<MyNotice>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter= MyNoticeViewAdapter(data_).apply{
                        setItemClickListener(
                            object : MyNoticeViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val notice_id=noticeList[position].id

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(this@ManageNotice,
                                        NoticeInfo::class.java)

                                    intent.apply {
                                        this.putExtra("club_id",getExtra())
                                        this.putExtra("notice_id",notice_id)// 데이터 넣기
                                    }
                                    startActivity(intent)
                                    onStop()
                                    //replaceFragment(GoodsInfoFragment())
                                }
                            })
                    }
                    //Toast.makeText(this@GoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MyNotice>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}