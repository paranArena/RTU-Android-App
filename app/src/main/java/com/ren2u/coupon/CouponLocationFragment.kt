package com.ren2u.coupon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ren2u.databinding.FragmentCouponLocationBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class CouponLocationFragment : Fragment() {
    private var _binding: FragmentCouponLocationBinding?=null

    private val binding get() = _binding!!

    var latitude=37.283672
    var longitude=127.045295

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCouponLocationBinding.inflate(inflater, container, false)

        val mapView = MapView(context)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        mapView.setZoomLevel(1, true);


        binding.frame.addView(mapView)

        binding.nextButton.setOnClickListener {
            val centerPoint=mapView.mapCenterPoint.mapPointGeoCoord
            latitude=centerPoint.latitude
            longitude=centerPoint.longitude
            (activity as AddCoupon).setLocation(latitude, longitude)

            activity?.onBackPressed()
        }

        return binding.root
    }
}