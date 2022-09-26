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
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.databinding.FragmentCouponInfoDialogBinding
import com.ren2u.model.CouponInfoResponse
import com.ren2u.model.MemberInfoModel
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CouponInfoDialog  : DialogFragment() {
    private var _binding: FragmentCouponInfoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCouponInfoDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val clubId = arguments?.getInt("clubId",0)
        val couponId = arguments?.getInt("couponId", 0)

        initView(clubId!!, couponId!!)

        binding.info.setOnClickListener {

        }

        binding.use.setOnClickListener {
            val dialog= CheckCouponDialog()

            val bundle=Bundle()
            bundle.putInt("clubId", clubId)
            bundle.putInt("couponId", couponId)
            dialog.arguments=bundle

            dialog.show(childFragmentManager, "CheckCouponDialog")
        }

        return view
    }

    private fun initView(clubId: Int, couponId: Int){
        RetrofitBuilder.api.getCouponUser(clubId, couponId).enqueue(object :
            Callback<CouponInfoResponse> {
            override fun onResponse(
                call: Call<CouponInfoResponse>,
                response: Response<CouponInfoResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    val data=response.body()

                    val name=data!!.data.name
                    val information=data!!.data.information
                    val imagePath=data!!.data.imagePath
                    val location=data!!.data.location.name
                    val actDate=data!!.data.actDate.substring(0 until 10).replace("-",". ")
                    val expDate=data!!.data.expDate.substring(0 until 10).replace("-",". ")

                    Glide.with(this@CouponInfoDialog).load(imagePath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.image)

                    binding.image.clipToOutline=true

                    binding.name.text=name
                    binding.detailText.text=information
                    binding.locationText.text=location
                    binding.dateText.text="$actDate ~ $expDate"

                }
                else {
                    Log.d("fail", response.code().toString())
                }
            }

            override fun onFailure(call: Call<CouponInfoResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}