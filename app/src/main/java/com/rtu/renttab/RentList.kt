package com.rtu.renttab

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.adapter.MyRentalAdapter
import com.rtu.databinding.FragmentRentListBinding

import com.rtu.model.*
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentList : Fragment() {
    private var _binding: FragmentRentListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<RentDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRentListBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler(){
        RetrofitBuilder.api.getMyRentals().enqueue(object :
            Callback<MyRentalResponse> {
            override fun onResponse(
                call: Call<MyRentalResponse>,
                response: Response<MyRentalResponse>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter= MyRentalAdapter(data_).apply{
                        setItemClickListener(
                            object : MyRentalAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val id=productList[position].id
                                }
                            })
                    }
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<MyRentalResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}