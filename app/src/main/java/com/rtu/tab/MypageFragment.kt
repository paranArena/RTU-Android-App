package com.rtu.tab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.databinding.FragmentMypageBinding
import com.rtu.model.MyInfoModel
import com.rtu.retrofit.RetrofitBuilder
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
}