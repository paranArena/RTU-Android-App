package com.ren2u.coupon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ren2u.R
import com.ren2u.databinding.ActivityCouponLocationBinding
import com.ren2u.databinding.ActivitySetLocationBinding
import com.ren2u.management.product.add.AddProduct4
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class CouponLocationActivity : AppCompatActivity() {
    private var _binding: ActivityCouponLocationBinding?=null

    private val binding get() = _binding!!

    private fun getLatitude(): Double? {
        return intent.getDoubleExtra("latitude", 37.283672)
    }

    private fun getLongitude(): Double? {
        return intent.getDoubleExtra("longitude", 127.045295)
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
        setContentView(R.layout.activity_set_location)

        _binding= ActivityCouponLocationBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        val mapView = MapView(this)

        val latitude=getLatitude()
        val longitude=getLongitude()

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
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

        //supportFragmentManager.beginTransaction().add(R.id.frame, MapsFragment()).commit()

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.nextButton.setOnClickListener {

            finish()
        }

        val view=binding.root
        setContentView(view)
    }
}