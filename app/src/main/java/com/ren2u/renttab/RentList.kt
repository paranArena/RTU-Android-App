package com.ren2u.renttab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ren2u.MainPageActivity
import com.ren2u.R
import com.ren2u.adapter.MyRentalAdapter
import com.ren2u.databinding.FragmentRentListBinding
import com.ren2u.management.member.MemberInfoFragment
import com.ren2u.management.product.add.SetLocation

import com.ren2u.model.*
import com.ren2u.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_main_page.*
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

                                    val name=productList[position].location.name
                                    val latitude=productList[position].location.latitude
                                    val longitude=productList[position].location.longitude
                                    val expDate= productList[position].rentalInfo.expDate
                                    val rentDate=productList[position].rentalInfo.rentDate
                                    val status=productList[position].rentalInfo.rentalStatus
                                    val clubId=productList[position].clubId
                                    val itemId=productList[position].id

                                    /*val dialog= LocationInfoFragment()

                                    val bundle=Bundle()

                                    bundle.putString("name", name)
                                    bundle.putDouble("latitude",latitude)
                                    bundle.putDouble("longitude",longitude)
                                    dialog.arguments=bundle*/

                                    val intent = Intent(context, LocationInfoActivity::class.java)

                                    intent.apply {
                                        this.putExtra("clubId", clubId)
                                        this.putExtra("latitude", latitude) // 데이터 넣기
                                        this.putExtra("name", name)
                                        this.putExtra("longitude", longitude)
                                        this.putExtra("expDate", expDate)
                                        this.putExtra("rentDate", rentDate)
                                        this.putExtra("status", status)
                                        this.putExtra("itemId", itemId)
                                    }

                                    (activity as MainPageActivity).startActivityForResult(intent, 2)

                                    /*childFragmentManager.beginTransaction().add(view,
                                        LocationInfoFragment()
                                    ).addToBackStack(null).commit()*/
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