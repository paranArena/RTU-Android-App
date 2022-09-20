package com.ren2u.renttab

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.ren2u.MainPageActivity
import com.ren2u.R
import com.ren2u.databinding.ActivityLocationInfoBinding
import com.ren2u.model.RentResponse
import com.ren2u.retrofit.RetrofitBuilder
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class LocationInfoActivity : AppCompatActivity() {

    private var _binding: ActivityLocationInfoBinding?=null

    private val binding get() = _binding!!

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    private var longitude: Double=0.0
    private var latitude: Double=0.0
    var distanceFrom by Delegates.notNull<Float>()

    private fun getName(): String? {
        return intent.getStringExtra("name")
    }

    private fun getLatitude(): Double? {
        return intent.getDoubleExtra("latitude", 0.0)
    }

    private fun getLongitude(): Double? {
        return intent.getDoubleExtra("longitude", 0.0)
    }

    private fun getClubId(): Int? {
        return intent.getIntExtra("clubId", 0)
    }

    private fun getItemId(): Int? {
        return intent.getIntExtra("itemId", 0)
    }

    private fun getStatus(): String? {
        return intent.getStringExtra("status")
    }

    private fun getExpDate(): String? {
        return intent.getStringExtra("expDate")
    }

    private fun getRentDate(): String? {
        return intent.getStringExtra("rentDate")
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

        _binding= ActivityLocationInfoBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        mLocationRequest =  LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }


        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }

        latitude=getLatitude()!!
        longitude=getLongitude()!!

        val name=getName()
        val clubId=getClubId()
        val itemId=getItemId()
        val expDate=getExpDate()
        val rentDate=getRentDate()
        val status=getStatus()

        binding.name.text=name

        if(status=="WAIT"){
            binding.returnButton.visibility= View.INVISIBLE
        } else if(status=="RENT"){
            binding.reserveCancel.visibility=View.INVISIBLE
            binding.reserveSet.visibility=View.INVISIBLE
        }

        binding.returnButton.setOnClickListener {
            startLocationUpdates()
            if(distanceFrom<30) {
                returnRent(clubId!!, itemId!!)
            } else{
                Toast.makeText(this, "위치가 너무 멉니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.reserveSet.setOnClickListener {
            startLocationUpdates()
            if(distanceFrom<30) {
                applyRent(clubId!!, itemId!!)
            } else{
                Toast.makeText(this, "위치가 너무 멉니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.reserveCancel.setOnClickListener {
            cancelRent(clubId!!, itemId!!)
        }

        val mapView = MapView(this)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
        mapView.setZoomLevel(1, true)

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

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val view=binding.root
        setContentView(view)
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

    private fun cancelRent(clubId: Int, itemId: Int){
        RetrofitBuilder.api.cancelRent(clubId, itemId).enqueue(object :
            Callback<RentResponse> {
            override fun onResponse(

                call: Call<RentResponse>,
                response: Response<RentResponse>
            ) {
                if (response.isSuccessful) {
                    showDialog("cancel")
                }
            }

            override fun onFailure(call: Call<RentResponse>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {

        mLastLocation = location

        var distance=getDistance(mLastLocation.latitude,mLastLocation.longitude,latitude,longitude)

        Log.d("test",mLastLocation.latitude.toString() + ","+ mLastLocation.longitude.toString())

        Log.d("test", distance.toString())

        distanceFrom=distance
        //mLastLocation.latitude // 갱신 된 위도
        //mLastLocation.longitude // 갱신 된 경도

    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this, "권한이 없어 현재 위치를 알 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (r * c)
    }*/

    private fun getDistance(lat1: Double, lng1:Double, lat2:Double, lng2:Double) : Float{

        val myLoc = Location(LocationManager.NETWORK_PROVIDER)
        val targetLoc = Location(LocationManager.NETWORK_PROVIDER)
        myLoc.latitude= lat1
        myLoc.longitude = lng1

        targetLoc.latitude= lat2
        targetLoc.longitude = lng2

        return myLoc.distanceTo(targetLoc)
    }

    fun showDialog(s: String){
        if(s=="apply") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("대여 확정")
                .setMessage("대여가 확정되었습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this@LocationInfoActivity, MainPageActivity::class.java)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
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
                        val intent = Intent(this@LocationInfoActivity, MainPageActivity::class.java)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
        if(s=="cancel") {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("취소 성공")
                .setMessage("예약이 취소되었습니다.")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        val intent = Intent(this@LocationInfoActivity, MainPageActivity::class.java)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }
}