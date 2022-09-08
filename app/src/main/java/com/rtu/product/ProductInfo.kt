package com.rtu.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.rtu.R
import com.rtu.adapter.ItemListAdapter
import com.rtu.adapter.MyNoticeViewAdapter
import com.rtu.databinding.ActivityProductInfoBinding
import com.rtu.model.ClubDetail
import com.rtu.model.CreateProductResponse
import com.rtu.model.GetProductResponse
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductInfo : AppCompatActivity() {
    private var _binding: ActivityProductInfoBinding?=null

    private val binding get() = _binding!!

    private fun getClubId(): Int {
        return intent.getIntExtra("clubId", 0)
    }

    private fun getProductId(): Int {
        return intent.getIntExtra("productId", 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        _binding= ActivityProductInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        binding.rentButton.bringToFront()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val clubId=getClubId()
        val productId=getProductId()

        getProductInfo(clubId, productId)

        val view=binding.root
        setContentView(view)
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
                    val name = data.data.name
                    val category = data.data.category
                    val price = data.data.price.toString()
                    val caution = data.data.caution
                    val imagePath = data.data.imagePath

                    binding.productNameText.text=name
                    binding.category.text=category
                    binding.price.text=price
                    binding.caution.text=caution

                    Glide.with(this@ProductInfo).load(imagePath).
                    placeholder(R.drawable.ic_launcher_foreground).into(binding.productImage)

                    binding.rvList.isNestedScrollingEnabled=false
                    binding.rvList.adapter= ItemListAdapter(data.data.items, name)

                }
            }

            override fun onFailure(call: Call<GetProductResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}