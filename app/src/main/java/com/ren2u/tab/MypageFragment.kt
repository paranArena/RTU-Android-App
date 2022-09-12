package com.ren2u.tab

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.ren2u.MainPageActivity
import com.ren2u.databinding.FragmentMypageBinding
import com.ren2u.model.BasicResponse
import com.ren2u.model.MyInfoModel
import com.ren2u.mypagetab.ConditionsActivity
import com.ren2u.mypagetab.MyInfoActivity
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

        getMyInfo()

        binding.profile.setOnClickListener {
            val intent = Intent(context, MyInfoActivity::class.java)
            startActivity(intent)
        }

        binding.deleteUser.setOnClickListener {
            dialog()
            activity?.finish()
        }

        binding.promise.setOnClickListener {
            val intent = Intent(context, ConditionsActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            (activity as MainPageActivity).deleteToken()
            activity?.finish()
        }

        return binding.root
    }

    private fun getMyInfo(){
        RetrofitBuilder.api.myInfoRequest().enqueue(object :
            Callback<MyInfoModel> {
            override fun onResponse(
                call: Call<MyInfoModel>,
                response: Response<MyInfoModel>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    val data=response.body()

                    val name=data!!.data.name
                    val major=data!!.data.major
                    val studentId=data!!.data.studentId.substring(0..1)

                    binding.name.text=name
                    binding.info.text="$major  $studentId 학번"

                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyInfoModel>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    private fun dialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("회원탈퇴")
            .setMessage("정말로 탈퇴하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                }).setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    private fun quitService(){
        RetrofitBuilder.api.quitService().enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                    activity?.finish()
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