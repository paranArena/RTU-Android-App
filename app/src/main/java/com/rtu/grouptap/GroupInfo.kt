package com.rtu.grouptap

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.rtu.R
import com.rtu.adapter.MyNoticeViewAdapter
import com.rtu.adapter.ProductHorizonAdapter
import com.rtu.databinding.ActivityGroupInfoBinding
import com.rtu.management.ManageActivity
import com.rtu.model.*
import com.rtu.retrofit.RetrofitBuilder
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
        getGroupPermission(id)
        getNotice(id)
        getProducts(id)

        binding.toolbarOption.setOnClickListener{
            when (binding.toolbarOption.text) {
                "관리자" -> {
                    val intent = Intent(this@GroupInfo, ManageActivity::class.java)
                    intent.apply {
                        this.putExtra("id", id) // 데이터 넣기
                    }
                    startActivity(intent)
                    finish()
                }
                "탈퇴하기" -> {
                    requestLeave(id)
                    finish()
                }
                "가입하기" -> {
                    requestJoin(id)
                }
                "신청취소" -> {
                    requestCancel(id)
                }
            }
        }

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

                    binding.toolbarTitle.text=title

                    Glide.with(this@GroupInfo).load(thumbnailPath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.groupImage)

                    var hashtags: String=""
                    for(tag in data.data.hashtags){
                        hashtags += "#"
                        hashtags += tag
                        hashtags += " "
                    }

                    binding.groupTagText.text=hashtags

                    binding.introText.text = introduction
                }
            }

            override fun onFailure(call: Call<ClubDetail>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getNotice(id: Int){
        RetrofitBuilder.api.getClubNotice(id).enqueue(object :
            Callback<MyNotice> {
            override fun onResponse(

                call: Call<MyNotice>,
                response: Response<MyNotice>
            ) {
                if (response.isSuccessful) {
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())


                    binding.rvList.adapter= MyNoticeViewAdapter(data.data).apply{
                        setItemClickListener(
                            object : MyNoticeViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val notice_id=noticeList[position].id

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(this@GroupInfo,
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

    private fun getProducts(id: Int){
        val data_ = mutableListOf<ProductDetail>()

        RetrofitBuilder.api.searchClubProductsAll(id).enqueue(object :
            Callback<GetProductModel> {
            override fun onResponse(
                call: Call<GetProductModel>,
                response: Response<GetProductModel>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvListHorizon.adapter= ProductHorizonAdapter(data_).apply{
                        setItemClickListener(
                            object : ProductHorizonAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    /*val id=productList[position].id
                                    val clubId=productList[position].clubId

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(this@GroupInfo, ProductInfo::class.java)

                                    intent.apply {
                                        this.putExtra("clubId",clubId)
                                        this.putExtra("productId",id) // 데이터 넣기
                                    }
                                    startActivity(intent)*/

                                }
                            })
                    }
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<GetProductModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getGroupPermission(id: Int){
        RetrofitBuilder.api.getMyClubRole(id).enqueue(object :
            Callback<MyRole> {
            override fun onResponse(

                call: Call<MyRole>,
                response: Response<MyRole>
            ) {
                if (response.isSuccessful) {

                    val data=response.body()!!
                    val role=data.data.clubRole
                    Log.d("test", role)
                    if(role=="OWNER" || role=="ADMIN"){
                        binding.toolbarOption.text="관리자"
                        binding.toolbarOption.visibility= View.VISIBLE
                    } else if(role=="USER"){
                        binding.toolbarOption.text="탈퇴하기"
                    } else if(role=="WAIT"){
                        binding.toolbarOption.text="신청취소"
                    } else{
                        binding.toolbarOption.text="가입하기"
                    }
                    //Toast.makeText(this@GoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
                else{
                    binding.toolbarOption.text="가입하기" // 나중에 삭제
                }

            }

            override fun onFailure(call: Call<MyRole>, t: Throwable) {
                Log.d("test", "실패$t")
                binding.toolbarOption.text="가입하기" // 나중에 삭제

                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun requestJoin(id: Int){
        RetrofitBuilder.api.getJoinClub(id).enqueue(object :
            Callback<JoinResponse> {
            override fun onResponse(

                call: Call<JoinResponse>,
                response: Response<JoinResponse>
            ) {
                if (response.isSuccessful) {
                    val data=response.body()!!

                    if(data.statusCode==200){
                        popUp("join")
                        binding.toolbarOption.text="신청취소"
                    }
                }
            }

            override fun onFailure(call: Call<JoinResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun requestCancel(id: Int){
        RetrofitBuilder.api.getJoinClubCancel(id).enqueue(object :
            Callback<JoinResponse> {
            override fun onResponse(

                call: Call<JoinResponse>,
                response: Response<JoinResponse>
            ) {
                if (response.isSuccessful) {
                    val data=response.body()!!

                    if(data.statusCode==200){
                        popUp("cancel")
                        binding.toolbarOption.text="가입하기"
                    }
                }
            }

            override fun onFailure(call: Call<JoinResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun requestLeave(id: Int){
        RetrofitBuilder.api.getLeaveClub(id).enqueue(object :
            Callback<JoinResponse> {
            override fun onResponse(

                call: Call<JoinResponse>,
                response: Response<JoinResponse>
            ) {
                if (response.isSuccessful) {
                    val data=response.body()!!
                    Log.d("test", data.toString())

                    if(data.statusCode==200){
                        popUp("leave")
                        binding.toolbarOption.text="가입하기"
                    }
                }
                else{
                    Log.d("test", response.code().toString())
                }
            }

            override fun onFailure(call: Call<JoinResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun popUp(s: String){
        if(s=="join") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("가입 요청 성공")
                .setMessage("관리자에게 가입 요청 하였습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
        if(s=="cancel") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("가입 취소 성공")
                .setMessage("가입 신청이 취소되었습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
        if(s=="leave") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("탈퇴 성공")
                .setMessage("그룹에서 탈퇴되었습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }
}