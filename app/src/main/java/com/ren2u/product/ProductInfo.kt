package com.ren2u.product


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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.ren2u.MainPageActivity
import com.ren2u.adapter.ItemListAdapter
import ren2u.databinding.ActivityProductInfoBinding
import com.ren2u.model.*
import com.ren2u.renttab.RentComplete
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.*
import kotlin.properties.Delegates


class ProductInfo : AppCompatActivity() {
    private var _binding: ActivityProductInfoBinding?=null

    private val binding get() = _binding!!
    private var buttonStatus=0
    private var selectedItemId=0
    private var moveInfo: String?=null

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10
    private val r = 6372.8 * 1000

    private var longitude: Double=0.0
    private var latitude: Double=0.0
    var distanceFrom by Delegates.notNull<Float>()


    private fun getClubId(): Int {
        return intent.getIntExtra("clubId", 0)
    }

    private fun getProductId(): Int {
        return intent.getIntExtra("productId", 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this@ProductInfo, MainPageActivity::class.java)
                setResult(Activity.RESULT_OK, intent)
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

        mLocationRequest =  LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }


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
                if(distanceFrom>30){
                    Toast.makeText(this@ProductInfo, "위치가 너무 멉니다.", Toast.LENGTH_SHORT).show()
                }else {
                    applyRent(clubId, selectedItemId)
                }
                buttonStatus=0
            }

            if(buttonStatus==3){//반납 하기
                if(distanceFrom>30){
                    Toast.makeText(this@ProductInfo, "위치가 너무 멉니다.", Toast.LENGTH_SHORT).show()
                }else {
                    returnRent(clubId, selectedItemId)
                }
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

                    latitude=data.data.location.latitude
                    longitude=data.data.location.longitude

                    if (checkPermissionForLocation(this@ProductInfo)) {
                        startLocationUpdates()
                    }

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
            val intent = Intent(this@ProductInfo, MainPageActivity::class.java)
            setResult(Activity.RESULT_OK, intent)
            finish()
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


    // 위치 권한이 있는지 확인하는 메서드
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

}