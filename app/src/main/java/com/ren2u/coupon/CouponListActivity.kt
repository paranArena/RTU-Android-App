package com.ren2u.coupon

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.ren2u.CustomDecoration
import com.ren2u.R
import com.ren2u.adapter.CouponListAdapter
import com.ren2u.adapter.MyNoticeViewAdapter
import com.ren2u.databinding.ActivityCouponListBinding
import com.ren2u.grouptap.NoticeInfo
import com.ren2u.model.CouponListModel
import com.ren2u.model.CouponListResponse
import com.ren2u.model.MyNotice
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponListActivity : AppCompatActivity() {

    private var _binding: ActivityCouponListBinding?=null

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

    override fun onResume() {
        getCoupon()
        getUsedCoupon()
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_list)

        _binding= ActivityCouponListBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        getCoupon()
        getUsedCoupon()

        val view=binding.root
        setContentView(view)
    }

    private fun getCoupon(){

        binding.rvList1.setDivider(3f,10f, getColor(R.color.lightGray))


        RetrofitBuilder.api.getMyCouponsAll().enqueue(object :
            Callback<CouponListResponse> {
            override fun onResponse(

                call: Call<CouponListResponse>,
                response: Response<CouponListResponse>
            ) {
                if (response.isSuccessful) {
                    val data_ = mutableListOf<CouponListModel>()
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList1.adapter= CouponListAdapter(data_, 1).apply{
                        setItemClickListener(
                            object : CouponListAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val couponId=couponList[position].id

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(this@CouponListActivity,
                                        NoticeInfo::class.java)

                                    intent.apply {
                                        this.putExtra("notice_id",couponId)// 데이터 넣기
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

            override fun onFailure(call: Call<CouponListResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUsedCoupon(){

        binding.rvList2.setDivider(3f,10f, getColor(R.color.lightGray))


        RetrofitBuilder.api.getMyCouponHistoriesAll().enqueue(object :
            Callback<CouponListResponse> {
            override fun onResponse(

                call: Call<CouponListResponse>,
                response: Response<CouponListResponse>
            ) {
                if (response.isSuccessful) {
                    val data_ = mutableListOf<CouponListModel>()
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList2.adapter= CouponListAdapter(data_, 2).apply{
                        setItemClickListener(
                            object : CouponListAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                }
                            })
                    }
                    //Toast.makeText(this@GoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CouponListResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun RecyclerView.setDivider(dividerHeight: Float, dividerPadding: Float, @ColorInt dividerColor: Int?) {
        val decoration = CustomDecoration(
            height = dividerHeight ?: 0f,
            padding = dividerPadding ?: 0f,
            color = dividerColor ?: Color.TRANSPARENT
        )

        addItemDecoration(decoration)
    }
}