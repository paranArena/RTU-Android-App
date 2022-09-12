package com.ren2u.management.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ren2u.adapter.ManageRentAdapter
import com.ren2u.databinding.FragmentManageRentedListBinding
import com.ren2u.model.ManageRentData
import com.ren2u.model.ManageRentModel
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageRentedList : Fragment()  {
    private var _binding: FragmentManageRentedListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<ManageRentData>()

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
        RetrofitBuilder.api.searchClubRentalsAll(id).enqueue(object :
            Callback<ManageRentModel> {
            override fun onResponse(
                call: Call<ManageRentModel>,
                response: Response<ManageRentModel>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        if(item.rentalInfo.rentalStatus=="RENT" || item.rentalInfo.rentalStatus=="LATE") {
                            data_.add(item)
                        }
                    }

                    binding.rvList.adapter= ManageRentAdapter(data_)
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<ManageRentModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}