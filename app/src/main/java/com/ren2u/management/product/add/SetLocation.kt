package com.ren2u.management.product.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ren2u.R
import com.ren2u.databinding.ActivitySetLocationBinding
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class SetLocation : AppCompatActivity(){
    private var _binding: ActivitySetLocationBinding?=null

    private val binding get() = _binding!!

    var latitude=37.283672
    var longitude=127.045295

    private fun getId(): Int {
        return intent.getIntExtra("id", 0)
    }

    private fun getFilePath(): String? {
        return intent.getStringExtra("filePath")
    }

    private fun getName(): String? {
        return intent.getStringExtra("name")
    }

    private fun getCategory(): String? {
        return intent.getStringExtra("category")
    }

    private fun getPrice(): String? {
        return intent.getStringExtra("price")
    }

    private fun getNumber(): String? {
        return intent.getStringExtra("number")
    }

    private fun getPeriod(): String? {
        return intent.getStringExtra("period")
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

        _binding= ActivitySetLocationBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        val mapView = MapView(this)

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        mapView.setZoomLevel(1, true);


        binding.frame.addView(mapView)

        //supportFragmentManager.beginTransaction().add(R.id.frame, MapsFragment()).commit()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.nextButton.setOnClickListener {
            val centerPoint=mapView.mapCenterPoint.mapPointGeoCoord
            latitude=centerPoint.latitude
            longitude=centerPoint.longitude
            val intent = Intent(this@SetLocation, AddProduct4::class.java)

            val id = getId()
            val filePath = getFilePath()
            val name = getName()
            val category = getCategory()
            val price = getPrice()
            val number = getNumber()
            val period = getPeriod()

            intent.apply {
                this.putExtra("id", id)
                this.putExtra("filePath", filePath) // 데이터 넣기
                this.putExtra("name", name)
                this.putExtra("category", category)
                this.putExtra("price", price)
                this.putExtra("number", number)
                this.putExtra("period", period)
                this.putExtra("latitude", latitude)
                this.putExtra("longitude",longitude)
            }

            startActivity(intent)
            finish()
        }

        val view=binding.root
        setContentView(view)
    }
}