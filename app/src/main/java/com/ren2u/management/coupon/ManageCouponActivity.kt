package com.ren2u.management.coupon

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
import com.ren2u.adapter.ManageCouponListAdapter
import com.ren2u.coupon.CouponInfoDialog
import com.ren2u.databinding.ActivityManageCouponBinding
import com.ren2u.model.CouponListModel
import com.ren2u.model.CouponListResponse
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageCouponActivity : AppCompatActivity() {

    private var _binding: ActivityManageCouponBinding? = null

    private val binding get() = _binding!!

    private fun getExtra(): Int {
        return intent.getIntExtra("id", 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_list)

        _binding = ActivityManageCouponBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val clubId=getExtra()

        getCoupons(clubId)

        val view = binding.root
        setContentView(view)
    }

    private fun getCoupons(clubId: Int){
        binding.rvList.setDivider(3f,10f, getColor(R.color.lightGray))


        RetrofitBuilder.api.getClubCouponAdmin(clubId).enqueue(object :
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

                    binding.rvList.adapter= ManageCouponListAdapter(data_).apply{
                        setItemClickListener(
                            object : ManageCouponListAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val couponId=couponList[position].id
                                    val clubId=couponList[position].clubId

                                    val dialog= CouponInfoDialog()

                                    val bundle=Bundle()
                                    bundle.putInt("clubId", clubId)
                                    bundle.putInt("couponId", couponId)
                                    dialog.arguments=bundle

                                    dialog.show(supportFragmentManager, "CouponInfoDialog")
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

    fun RecyclerView.setDivider(dividerHeight: Float, dividerPadding: Float, @ColorInt dividerColor: Int?) {
        val decoration = CustomDecoration(
            height = dividerHeight ?: 0f,
            padding = dividerPadding ?: 0f,
            color = dividerColor ?: Color.TRANSPARENT
        )

        addItemDecoration(decoration)
    }
}