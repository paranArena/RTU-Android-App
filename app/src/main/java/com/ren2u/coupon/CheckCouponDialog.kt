package com.ren2u.coupon

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ren2u.R
import com.ren2u.databinding.FragmentCheckCouponDialogBinding
import com.ren2u.model.BasicResponse
import com.ren2u.model.UseCouponResponse
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckCouponDialog : DialogFragment() {
    private var _binding: FragmentCheckCouponDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCheckCouponDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val clubId = arguments?.getInt("clubId",0)
        val couponId = arguments?.getInt("couponId", 0)

        binding.yes.setOnClickListener {
            useCoupon(clubId!!, couponId!!)
        }

        binding.no.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun useCoupon(clubId: Int, couponId: Int){
        RetrofitBuilder.api.useCouponUser(clubId, couponId).enqueue(object :
            Callback<UseCouponResponse> {
            override fun onResponse(
                call: Call<UseCouponResponse>,
                response: Response<UseCouponResponse>
            ) {
                if(response.isSuccessful) {
                    activity?.onBackPressed()
                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<UseCouponResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }
}