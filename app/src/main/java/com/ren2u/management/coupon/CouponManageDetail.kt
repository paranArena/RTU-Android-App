package com.ren2u.management.coupon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.ren2u.R
import com.ren2u.databinding.ActivityCouponManageDetailBinding
import com.ren2u.grouptap.NoticeInfo
import com.ren2u.model.AdminCouponResponse
import com.ren2u.renttab.RentList
import com.ren2u.renttab.RentProducts
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponManageDetail : AppCompatActivity() {

    private var _binding: ActivityCouponManageDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout

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

    override fun onResume() {
        super.onResume()
        val clubId=getClubId()
        val couponId=getCouponId()

        replaceFragment(UnusedCouponList())

        getCoupon(clubId, couponId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_manage_detail)

        _binding = ActivityCouponManageDetailBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        tabLayout = binding.tabLayout

        replaceFragment(UnusedCouponList())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> replaceFragment(UnusedCouponList())
                    1 -> replaceFragment(UsedCouponList())
                }
            }
        })

        val clubId=getClubId()
        val couponId=getCouponId()

        getCoupon(clubId, couponId)

        binding.selectMember.setOnClickListener {
            val intent = Intent(this@CouponManageDetail,
                SelectMemberActivity::class.java)

            intent.apply {
                this.putExtra("clubId",clubId)
                this.putExtra("couponId",couponId)// 데이터 넣기
            }
            startActivity(intent)
            onStop()
        }

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

    fun replaceFragment(fragment: Fragment) {

        val clubId=getClubId()
        val couponId=getCouponId()
        val bundle=Bundle()
        bundle.putInt("clubId", clubId)
        bundle.putInt("couponId", couponId)

        fragment.arguments=bundle
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
    }
}