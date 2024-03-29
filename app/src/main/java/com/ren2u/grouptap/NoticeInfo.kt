package com.ren2u.grouptap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.databinding.ActivityNoticeInfoBinding
import com.ren2u.management.notice.ChangeNoticeActivity
import com.ren2u.model.NoticeInfoModel
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeInfo : AppCompatActivity() {

    private var _binding: ActivityNoticeInfoBinding?=null

    private val binding get() = _binding!!

    private fun getClubExtra(): Int {
        return intent.getIntExtra("club_id", 0)
    }

    private fun getNoticeExtra(): Int {
        return intent.getIntExtra("notice_id", 0)
    }

    private fun getPermissionExtra(): Int {
        return intent.getIntExtra("permission", 0)
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
        setContentView(R.layout.activity_notice_info)

        _binding= ActivityNoticeInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val club_id=getClubExtra()
        val notice_id=getNoticeExtra()
        getNoticeInfo(club_id, notice_id)

        binding.toolbarOption.visibility= View.INVISIBLE

        val permission=getPermissionExtra()
        if(permission==1){
            binding.toolbarOption.visibility= View.VISIBLE
        }

        binding.toolbarOption.setOnClickListener {
            val intent = Intent(this@NoticeInfo,
                ChangeNoticeActivity::class.java)

            intent.apply {
                this.putExtra("club_id", club_id)
                this.putExtra("notice_id",notice_id)// 데이터 넣기
            }
            startActivity(intent)
            finish()
        }

        /*binding.toolbarOption.setOnClickListener{
            val intent = Intent(this@NoticeInfo, ManageActivity::class.java)
            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }
            startActivity(intent)
        }*/

        val view=binding.root
        setContentView(view)
    }

    private fun getNoticeInfo(club_id: Int, notice_id: Int){
        RetrofitBuilder.api.getNoticeDetail(club_id, notice_id).enqueue(object :
            Callback<NoticeInfoModel> {
            override fun onResponse(

                call: Call<NoticeInfoModel>,
                response: Response<NoticeInfoModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val title = data.data.title
                    val content = data.data.content
                    val imagePath = data.data.imagePath
                    val createdAt = data.data.createdAt.substring(0 until 10).replace("-",". ")

                    Glide.with(this@NoticeInfo).load(imagePath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.noticeImage)

                    binding.noticeImage.clipToOutline=true

                    binding.titleText.text = title
                    binding.contentText.text = content
                    binding.createdAt.text= createdAt
                }
            }

            override fun onFailure(call: Call<NoticeInfoModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}