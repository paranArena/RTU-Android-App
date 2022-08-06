package com.rtu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rtu.tab.GroupFragment
import com.rtu.tab.HomeFragment
import com.rtu.tab.MypageFragment
import com.rtu.tab.RentFragment

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