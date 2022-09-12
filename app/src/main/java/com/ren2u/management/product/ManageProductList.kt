package com.ren2u.management.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ren2u.adapter.MyProductAdapter

import ren2u.databinding.FragmentManageProductListBinding

import com.ren2u.management.product.add.AddProduct
import com.ren2u.model.GetProductModel
import com.ren2u.model.ProductDetail
import com.ren2u.product.ProductInfo
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageProductList : Fragment() {
    private var _binding: FragmentManageProductListBinding? = null

    private val binding get() = _binding!!

    val data_ = mutableListOf<ProductDetail>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentManageProductListBinding.inflate(inflater, container, false)

        val id=arguments?.getInt("id")

        initRecycler(id!!)

        binding.addProduct.setOnClickListener {
            val intent = Intent(activity, AddProduct::class.java)

            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }

            startActivity(intent)
        }


        return binding.root
    }

    private fun initRecycler(id: Int){
        RetrofitBuilder.api.searchClubProductsAll(id).enqueue(object :
            Callback<GetProductModel> {
            override fun onResponse(
                call: Call<GetProductModel>,
                response: Response<GetProductModel>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter= MyProductAdapter(data_).apply{
                        setItemClickListener(
                            object : MyProductAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val id=productList[position].id
                                    val clubId=productList[position].clubId

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(context, ProductInfo::class.java)

                                    intent.apply {
                                        this.putExtra("clubId",clubId)
                                        this.putExtra("productId",id) // 데이터 넣기
                                    }
                                    startActivity(intent)

                                }
                            })
                    }
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<GetProductModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}