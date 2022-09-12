package com.ren2u.product

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import ren2u.R
import com.ren2u.adapter.ItemListAdapter

import ren2u.databinding.FragmentSelectProductBinding

import com.ren2u.model.*
import com.ren2u.retrofit.RetrofitBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelectProductFragment : Fragment()  {

    private var _binding: FragmentSelectProductBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    private var data_=mutableListOf<ItemsModel>()
    private lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectProductBinding.inflate(inflater, container, false)

        //Log.d("test", "before")

        var clubId: Int?=null
        var productId: Int?=null

        arguments?.let{
            clubId=it.getInt("clubId")
            productId=it.getInt("productId")
        }

        getProductInfo(clubId!!, productId!!)

        var selectedItemId=0

        GlobalScope.launch {
            delay(1000)

            activity?.runOnUiThread {
                binding.rvList.adapter = ItemListAdapter(data_,name).apply {
                    setItemClickListener(
                        object : ItemListAdapter.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                if(selectedItemId==0){
                                    selectedItemId=itemList[position].id
                                    val selectedItem=itemList[position]

                                    view.findViewById<View>(R.id.backgroundView).setBackgroundResource(R.color.deepBlue)
                                    view.findViewById<TextView>(R.id.iv_name).setTextColor(Color.WHITE)
                                    view.findViewById<TextView>(R.id.iv_status).setTextColor(Color.WHITE)

                                    (activity as ProductInfo).changeButton("selected", selectedItem)

                                } else{
                                    if(selectedItemId==itemList[position].id){
                                        view.findViewById<View>(R.id.backgroundView).setBackgroundResource(R.color.lightGray)
                                        view.findViewById<TextView>(R.id.iv_name).setTextColor(Color.BLACK)
                                        view.findViewById<TextView>(R.id.iv_status).setTextColor(
                                            resources.getColor(R.color.darkGray))
                                        selectedItemId=0

                                        (activity as ProductInfo).changeButton("wait", null)
                                    }
                                }
                            }
                        })
                }
            }
        }

        return binding.root
    }

    private fun getProductInfo(clubId: Int, productId: Int){
        RetrofitBuilder.api.getProduct(clubId, productId).enqueue(object :
            Callback<GetProductResponse> {
            override fun onResponse(

                call: Call<GetProductResponse>,
                response: Response<GetProductResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", response.body().toString())
                    val data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    name = data.data.name

                    for (item in data.data.items) {
                        data_.add(item)
                    }

                    /*binding.rvList.isNestedScrollingEnabled=false
                    binding.rvList.adapter= ItemListAdapter(data.data.items, name)*/

                }
            }

            override fun onFailure(call: Call<GetProductResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}