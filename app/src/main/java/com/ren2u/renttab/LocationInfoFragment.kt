package com.ren2u.renttab

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ren2u.R
import com.ren2u.databinding.FragmentLocationInfoBinding
import com.ren2u.databinding.FragmentMemberInfoBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class LocationInfoFragment : DialogFragment() {
    private var _binding: FragmentLocationInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val name = arguments?.getString("name")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        initView(name!!, latitude!!, longitude!!)


        //val email=arguments?.getString("email")
        //getMemberInfo(email!!)

        /*binding.admin.setOnClickListener {
            val clubRole=binding.admin.text

            if(clubRole=="관리자 권한"){
                grantAdmin(clubId!!,memberId!!)
            } else{
                grantUser(clubId!!,memberId!!)
            }
        }*/

        return view
    }

    private fun initView(name: String, latitude: Double, longitude: Double){
        val mapView = MapView(context)

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

        binding.name.text=name

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}