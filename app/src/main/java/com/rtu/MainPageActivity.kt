package com.rtu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rtu.model.LoginResponse
import com.rtu.model.MyInfoModel
import com.rtu.retrofit.RetrofitBuilder
import com.rtu.tab.GroupFragment
import com.rtu.tab.HomeFragment
import com.rtu.tab.MypageFragment
import com.rtu.tab.RentFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPageActivity : AppCompatActivity() {
    private val frame: ConstraintLayout by lazy {
        findViewById(R.id.body_container)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().add(frame.id, HomeFragment()).commit()

        RetrofitBuilder.api.myInfoRequest().enqueue(object :
            Callback<MyInfoModel> {
            override fun onResponse(
                call: Call<MyInfoModel>,
                response: Response<MyInfoModel>
            ) {
                if(response.isSuccessful) {
                    Log.d("test", response.body().toString())

                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyInfoModel>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
        // 하단 네비게이션 바 클릭 이벤트 설정
        bottomNagivationView.setOnItemSelectedListener {item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_group -> {
                    replaceFragment(GroupFragment())
                    true
                }
                R.id.nav_rent -> {
                    replaceFragment(RentFragment())
                    true
                }
                R.id.nav_mypage -> {
                    replaceFragment(MypageFragment())
                    true
                }
                else -> false
            }
        }

    }

    // 화면 전환 구현 메소드
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()
    }

    //백 버튼 이벤트
    var backKeyPressedTime : Long=0
    override fun onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime+2500){
            backKeyPressedTime=System.currentTimeMillis()
            return
        }

        else{
            finishAffinity()
        }
    }
}