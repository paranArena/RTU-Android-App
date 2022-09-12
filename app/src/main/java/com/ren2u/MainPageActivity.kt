package com.ren2u

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ren2u.tab.GroupFragment
import com.ren2u.tab.MypageFragment
import com.ren2u.tab.RentFragment
import com.ren2u.R
import java.security.MessageDigest

@Suppress("DEPRECATION")
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
        supportFragmentManager.beginTransaction().replace(frame.id, GroupFragment()).commit()


        // 하단 네비게이션 바 클릭 이벤트 설정
        bottomNagivationView.setOnItemSelectedListener {item ->
            when(item.itemId) {
                /*R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }*/
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

        getAppKeyHash()

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

    fun deleteToken(){
        MainActivity.GlobalApplication.prefs.removeString("token")
    }

    fun getAppKeyHash() {
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {

            Log.e("name not found", e.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                replaceFragment(RentFragment())
            }
        }
    }
}