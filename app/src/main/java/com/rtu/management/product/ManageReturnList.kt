package com.rtu.management.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.R
import com.rtu.adapter.ManageRentAdapter
import com.rtu.adapter.ReturnLogAdapter
import com.rtu.databinding.FragmentManageRentedListBinding
import com.rtu.model.ManageRentData
import com.rtu.model.ManageRentModel
import com.rtu.model.ReturnLog
import com.rtu.model.ReturnModel
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageReturnList : Fragment() {
    private var _binding: FragmentManageRentedListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<ReturnLog>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManageRentedListBinding.inflate(inflater, container, false)

        val id=arguments?.getInt("id")

        initRecycler(id!!)

        return binding.root
    }

    private fun initRecycler(id: Int){
        RetrofitBuilder.api.searchClubReturnAll(id).enqueue(object :
            Callback<ReturnModel> {
            override fun onResponse(
                call: Call<ReturnModel>,
                response: Response<ReturnModel>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter= ReturnLogAdapter(data_)
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<ReturnModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}