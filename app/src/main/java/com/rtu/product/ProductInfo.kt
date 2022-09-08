package com.rtu.product


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
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
    private var buttonStatus=0
    private var selectedItemId=0
    private var moveInfo: String?=null

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


        binding.rentButton.setOnClickListener {
            val selectProductFragment=SelectProductFragment()
            val bundle=Bundle()

            bundle.putInt("clubId", clubId)
            bundle.putInt("productId",productId)

            selectProductFragment.arguments=bundle

            if(buttonStatus==0) {
                if(moveInfo==null) {
                    addFragment(selectProductFragment)
                }
            }

            if(buttonStatus==1) {

            }
        }

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

                    var number=0

                    for(item in data.data.items){
                        if(item.rentalInfo==null) number++
                    }

                    binding.helpText.text="대여가능 수량: $number"

                    binding.rvList.isNestedScrollingEnabled=false
                    binding.rvList.adapter= ItemListAdapter(data.data.items, name).apply {
                        setItemClickListener(
                            object : ItemListAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {

                                }
                            })
                    }

                }
            }

            override fun onFailure(call: Call<GetProductResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        val selectProductFragment=SelectProductFragment()
        if(moveInfo!=null) {
            removeFragment(selectProductFragment)
        }
        else{
            super.onBackPressed()
        }
    }

    private fun addFragment(fragment: Fragment) {
        moveInfo="moved"
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment).addToBackStack("fragment")
            .commit()
    }

    private fun removeFragment(fragment: Fragment) {
        moveInfo=null
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.popBackStack()
    }

    fun changeButton(s: String, id: Int){
        if(s=="selected") {
            binding.rentButton.setImageResource(R.drawable.ic_rented_button)
            buttonStatus=1
            selectedItemId=id
        }
        if(s=="wait"){
            binding.rentButton.setImageResource(R.drawable.ic_rent_button)
            buttonStatus=0
        }
    }
}