package com.ren2u.management.coupon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.databinding.ActivityCouponManageDetailBinding
import com.ren2u.model.AdminCouponResponse
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponManageDetail : AppCompatActivity() {

    private var _binding: ActivityCouponManageDetailBinding? = null

    private val binding get() = _binding!!

    private fun getClubId(): Int {
        return intent.getIntExtra("clubId", 0)
    }

    private fun getCouponId(): Int {
        return intent.getIntExtra("couponId", 0)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_manage_detail)

        _binding = ActivityCouponManageDetailBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val clubId=getClubId()
        val couponId=getCouponId()

        getCoupon(clubId, couponId)

        val view = binding.root
        setContentView(view)
    }

    private fun getCoupon(clubId: Int, couponId: Int){
        RetrofitBuilder.api.getCouponAdmin(clubId, couponId).enqueue(object :
            Callback<AdminCouponResponse> {
            override fun onResponse(

                call: Call<AdminCouponResponse>,
                response: Response<AdminCouponResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    val name = data.data.name
                    val information = data.data.information
                    val allCouponCount = data.data.allCouponCount.toString()
                    val leftCouponCount = data.data.leftCouponCount.toString()
                    val imagePath = data.data.imagePath
                    val location = data.data.location.name
                    val actDate = data.data.actDate.substring(0 until 10).replace("-",". ")
                    val expDate = data.data.expDate.substring(0 until 10).replace("-",". ")

                    Glide.with(this@CouponManageDetail).load(imagePath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.couponImage)

                    binding.couponImage.clipToOutline=true

                    binding.couponName.text=name
                    binding.detail.text=information
                    binding.number.text="$allCouponCount 장"
                    binding.unused.text="$leftCouponCount 장"
                    binding.location.text=location
                    binding.date.text="$actDate ~ $expDate"
                }
            }

            override fun onFailure(call: Call<AdminCouponResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}