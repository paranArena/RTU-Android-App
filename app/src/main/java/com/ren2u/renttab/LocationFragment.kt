package com.ren2u.renttab

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ren2u.R

class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var finalMarker: LatLng

    var latitude: Double=0.0
    var longitude: Double=0.0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*latitude=requireArguments().getDouble("latitude")
        longitude= requireArguments().getDouble("longitude")

        Log.d("test", latitude.toString()+longitude.toString())*/

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap=googleMap

        val marker = LatLng(37.283672, 127.045295)
        finalMarker=marker
        mMap.addMarker(MarkerOptions().position(marker)
            .title("대여 위치"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 18.0f))
        mMap.uiSettings.isMapToolbarEnabled=false
    }
}