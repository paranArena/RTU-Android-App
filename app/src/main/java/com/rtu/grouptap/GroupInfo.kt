package com.rtu.grouptap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.rtu.R
import com.rtu.adapter.MyNoticeViewAdapter
import com.rtu.databinding.ActivityGroupInfoBinding
import com.rtu.model.ClubDetail
import com.rtu.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_add_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupInfo : AppCompatActivity() {

    private var _binding: ActivityGroupInfoBinding?=null

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
        setContentView(R.layout.activity_group_info)

        _binding= ActivityGroupInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val id=getExtra()
        getGroupInfo(id)

        val view=binding.root
        setContentView(view)
    }

    private fun getGroupInfo(id: Int){
        RetrofitBuilder.api.getGroupDetail(id).enqueue(object :
            Callback<ClubDetail> {
            override fun onResponse(

                call: Call<ClubDetail>,
                response: Response<ClubDetail>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val title = data.data.name
                    val introduction = data.data.introduction
                    val thumbnailPath = data.data.thumbnailPath

                    val noticeList=data.data.memberList

                    binding.toolbarTitle.text=title

                    Glide.with(this@GroupInfo).load(thumbnailPath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.groupImage)

                    binding.introText.text = introduction
                    if(data.data.notifications!= null) {
                        binding.rvList.adapter = MyNoticeViewAdapter(data.data.notifications)
                    }

                    //Toast.makeText(this@GoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ClubDetail>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}