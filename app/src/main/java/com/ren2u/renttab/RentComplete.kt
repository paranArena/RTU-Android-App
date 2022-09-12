package com.ren2u.renttab

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ren2u.R
import com.ren2u.databinding.ActivityRentCompleteBinding
import com.ren2u.model.GetProductResponse
import com.ren2u.retrofit.RetrofitBuilder
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
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

                    val mapView = MapView(this@RentComplete)

                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                    mapView.setZoomLevel(1, true);

                    val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)

                    val marker = MapPOIItem()
                    marker.itemName = "대여 위치"
                    marker.tag = 0
                    marker.mapPoint = mapPoint
                    marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

                    marker.selectedMarkerType =
                        MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.


                    mapView.addPOIItem(marker)

                    binding.frame.addView(mapView)


                    //supportFragmentManager.beginTransaction().add(R.id.frame, LocationFragment()).commit()

                }
            }

            override fun onFailure(call: Call<GetProductResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}