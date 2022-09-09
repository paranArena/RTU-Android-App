package com.rtu.product


import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.rtu.R
import com.rtu.adapter.ItemListAdapter
import com.rtu.adapter.MyNoticeViewAdapter
import com.rtu.databinding.ActivityProductInfoBinding
import com.rtu.management.AddNotice
import com.rtu.model.*
import com.rtu.renttab.RentComplete
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

            if(buttonStatus==0) {// 처음
                if(moveInfo==null) {
                    addFragment(selectProductFragment)
                }
            }

            if(buttonStatus==1) {//대여 하기
                getRent(clubId,selectedItemId)
                buttonStatus=0
            }

            if(buttonStatus==2){//대여 확정
                applyRent(clubId,selectedItemId)
                buttonStatus=0
            }

            if(buttonStatus==3){//대여 확정
                returnRent(clubId,selectedItemId)
                buttonStatus=0
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

    private fun getRent(clubId: Int, itemId: Int){
        RetrofitBuilder.api.requestRent(clubId, itemId).enqueue(object :
            Callback<RequestRentResponse> {
            override fun onResponse(

                call: Call<RequestRentResponse>,
                response: Response<RequestRentResponse>
            ) {
                if (response.isSuccessful) {
                    val data=response.body()!!
                    val intent = Intent(this@ProductInfo, RentComplete::class.java)

                    val productId=getProductId()

                    intent.apply {
                        this.putExtra("name",data.data.numbering.toString()) // 데이터 넣기
                        this.putExtra("id",data.data.id)
                        this.putExtra("clubId",data.data.id)
                        this.putExtra("productId",productId)
                    }

                    startActivity(intent)

                    onBackPressed()

                }
            }

            override fun onFailure(call: Call<RequestRentResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun applyRent(clubId: Int, itemId: Int){

        RetrofitBuilder.api.applyRent(clubId, itemId).enqueue(object :
            Callback<RentResponse> {
            override fun onResponse(

                call: Call<RentResponse>,
                response: Response<RentResponse>
            ) {
                if (response.isSuccessful) {
                    showDialog("apply")
                }
            }

            override fun onFailure(call: Call<RentResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun returnRent(clubId: Int, itemId: Int){
        RetrofitBuilder.api.returnRent(clubId, itemId).enqueue(object :
            Callback<RentResponse> {
            override fun onResponse(

                call: Call<RentResponse>,
                response: Response<RentResponse>
            ) {
                if (response.isSuccessful) {
                    showDialog("return")
                }
            }

            override fun onFailure(call: Call<RentResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        val selectProductFragment=SelectProductFragment()
        if(moveInfo!=null) {
            removeFragment(selectProductFragment)
            binding.rentButton.setImageResource(R.drawable.ic_rent_button)
            buttonStatus=0
            val clubId=getClubId()
            val productId=getProductId()

            getProductInfo(clubId, productId)
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

    fun changeButton(s: String, selectedItem: ItemsModel?){
        if(s=="selected") {

            if(selectedItem!!.rentalInfo==null){
                buttonStatus=1
                binding.rentButton.setImageResource(R.drawable.ic_rented_button)
            } else if(selectedItem!!.rentalInfo.meRental){
                val status=selectedItem!!.rentalInfo.rentalStatus
                if(status=="WAIT"){
                    buttonStatus=2
                    binding.rentButton.setImageResource(R.drawable.ic_rent_set_button)
                }
                if(status=="RENT" || status=="LATE"){
                    buttonStatus=3
                    binding.rentButton.setImageResource(R.drawable.ic_return_button)
                }
            } else{
                buttonStatus=0
            }


            selectedItemId=selectedItem!!.id
        }
        if(s=="wait"){
            binding.rentButton.setImageResource(R.drawable.ic_rent_button)
            buttonStatus=0
        }
    }

    fun showDialog(s: String){
        if(s=="apply") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("대여 확정")
                .setMessage("대여가 확정되었습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        this@ProductInfo.onBackPressed()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }

        if(s=="return") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("반납 성공")
                .setMessage("물건이 정상적으로 반납되었습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        this@ProductInfo.onBackPressed()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }


}