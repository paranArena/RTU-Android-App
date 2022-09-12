package com.rtu.management.product.add

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var finalMarker: LatLng

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap=googleMap
        /*val marker = OnMapReadyCallback { googleMap ->
            val init = LatLng(37.283672, 127.045295)
            googleMap.addMarker(MarkerOptions().position(init)
                .title("반납 위치 (길게 눌러서 드래그)")
                .draggable(true))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(init, 18.0f))
            googleMap.uiSettings.isMapToolbarEnabled=false
        }*/

        val marker = LatLng(37.283672, 127.045295)
        finalMarker=marker
        mMap.addMarker(MarkerOptions().position(marker)
            .title("반납 위치 (길게 눌러서 드래그)")
            .draggable(true))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 18.0f))
        mMap.uiSettings.isMapToolbarEnabled=false

        with(mMap){
            setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    finalMarker=marker.position
                }

                override fun onMarkerDrag(marker: Marker) {
                }
            })
        }
    }
}