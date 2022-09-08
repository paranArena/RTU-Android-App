package com.rtu.renttab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.rtu.R
import com.rtu.databinding.ActivityRentCompleteBinding
import com.rtu.model.GetProductResponse
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RentComplete : AppCompatActivity() {
    private var _binding: ActivityRentCompleteBinding?=null

    private val binding get() = _binding!!

    private fun getClubId(): Int {
        return intent.getIntExtra("clubId", 0)
    }

    private fun getProductId(): Int {
        return intent.getIntExtra("productId", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_complete)

        _binding= ActivityRentCompleteBinding.inflate(layoutInflater)

        binding.nextButton.setOnClickListener {
            finish()
        }

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
                    val period = data.data.fifoRentalPeriod.toString()
                    val location = data.data.location.name
                    val latitude=data.data.location.latitude
                    val longitude=data.data.location.longitude

                    binding.name.text="물품이름 : $name"
                    binding.period.text="대여기간 : $period 일"
                    binding.location.text="$location 에서 픽업해 주세요"

                    val locationFragment=LocationFragment()
                    val bundle=Bundle()

                    bundle.putDouble("latitude", latitude)
                    bundle.putDouble("longitude",longitude)

                    locationFragment.arguments=bundle

                    supportFragmentManager.beginTransaction().add(R.id.frame, LocationFragment()).commit()

                }
            }

            override fun onFailure(call: Call<GetProductResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}