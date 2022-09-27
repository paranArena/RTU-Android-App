package com.ren2u.management.coupon

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ren2u.CustomDecoration
import com.ren2u.R
import com.ren2u.adapter.UnusedCouponAdapter
import com.ren2u.databinding.FragmentUnusedCouponListBinding
import com.ren2u.model.CouponMemberModel
import com.ren2u.model.CouponMemberResponse
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnusedCouponList : Fragment() {
    private var _binding: FragmentUnusedCouponListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<CouponMemberModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnusedCouponListBinding.inflate(inflater, container, false)

        val clubId=arguments?.getInt("clubId")
        val couponId=arguments?.getInt("couponId")

        initRecycler(clubId!!, couponId!!)

        return binding.root
    }

    private fun initRecycler(clubId: Int, couponId: Int){
        binding.rvList.setDivider(3f,10f, context?.let {
            ContextCompat.getColor(
                it,
                R.color.lightGray
            )
        })

        RetrofitBuilder.api.getCouponMembersAdmin(clubId, couponId).enqueue(object :
            Callback<CouponMemberResponse> {
            override fun onResponse(
                call: Call<CouponMemberResponse>,
                response: Response<CouponMemberResponse>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter= UnusedCouponAdapter(data_)
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<CouponMemberResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
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