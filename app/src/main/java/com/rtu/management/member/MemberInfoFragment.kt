package com.rtu.management.member

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rtu.databinding.FragmentMemberInfoBinding
import com.rtu.model.BasicResponse
import com.rtu.model.MemberInfoModel
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberInfoFragment : DialogFragment() {
    private var _binding: FragmentMemberInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMemberInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val clubId = arguments?.getInt("clubId",0)
        val memberId = arguments?.getInt("memberId", 0)

        initView()
        //val email=arguments?.getString("email")
        //getMemberInfo(email!!)

        /*binding.admin.setOnClickListener {
            val clubRole=binding.admin.text

            if(clubRole=="관리자 권한"){
                grantAdmin(clubId!!,memberId!!)
            } else{
                grantUser(clubId!!,memberId!!)
            }
        }*/

        binding.delete.setOnClickListener {
           removeMember(clubId!!,memberId!!)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(){
        val name=arguments?.getString("name")
        val major=arguments?.getString("major")
        val studentId=arguments?.getString("studentId")
        val email=arguments?.getString("email")
        val phoneNumber=arguments?.getString("number")
        val clubRole=arguments?.getString("clubRole")

        binding.name.text=name
        binding.numberText.text=phoneNumber
        binding.emailText.text=email
        val majorId="$major $studentId 학번"
        binding.majorId.text=majorId



        if(clubRole=="ADMIN"){
            //binding.admin.visibility=View.VISIBLE
            binding.label.visibility=View.VISIBLE
            //binding.admin.text="일반회원 변경"
        } else if(clubRole=="OWNER"){
            //binding.admin.visibility=View.INVISIBLE
            binding.label.visibility=View.VISIBLE
            binding.delete.visibility=View.INVISIBLE
        }
        else{
            //binding.admin.text="관리자 권한"
            binding.label.visibility=View.INVISIBLE
        }
    }

    private fun getMemberInfo(email: String){
        RetrofitBuilder.api.getMemberInfo(email).enqueue(object :
            Callback<MemberInfoModel> {
            override fun onResponse(
                call: Call<MemberInfoModel>,
                response: Response<MemberInfoModel>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    val data=response.body()

                    val name=data!!.data.name
                    val major=data!!.data.major
                    val studentId=data!!.data.studentId.substring(0..1)
                    val email=data!!.data.email
                    val phoneNumber=data!!.data.phoneNumber

                    binding.name.text=name
                    binding.numberText.text=phoneNumber
                    binding.emailText.text=email
                    val majorId="$major $studentId 학번"
                    binding.majorId.text=majorId

                    val clubRole=data.data.authorities[0].authorityName

                    if(clubRole=="ROLE_ADMIN" || clubRole=="ROLE_OWNER"){
                        //binding.admin.visibility=View.VISIBLE
                        binding.label.visibility=View.VISIBLE
                    } else{
                        //binding.admin.text="관리자 권한"
                        binding.label.visibility=View.INVISIBLE
                    }

                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MemberInfoModel>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    private fun grantAdmin(clubId: Int, memberId: Int){
        RetrofitBuilder.api.grantAdmin(clubId, memberId).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful) {
                   // binding.admin.text="일반회원 변경"
                    binding.label.visibility=View.VISIBLE
                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    private fun grantUser(clubId: Int, memberId: Int){
        RetrofitBuilder.api.grantUser(clubId, memberId).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful) {
                   //     binding.admin.text="관리자 권한"
                        binding.label.visibility=View.INVISIBLE
                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    private fun removeMember(clubId: Int, memberId: Int){
        RetrofitBuilder.api.removeMember(clubId, memberId).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful) {
                    activity?.onBackPressed()
                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }
}