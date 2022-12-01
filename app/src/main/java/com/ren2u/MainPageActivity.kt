package com.ren2u

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AlertDialog

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ren2u.tab.GroupFragment
import com.ren2u.tab.MypageFragment
import com.ren2u.tab.RentFragment
import com.ren2u.R
import com.ren2u.model.FcmModel
import com.ren2u.model.MyInfoModel
import com.ren2u.renttab.RentList
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import kotlin.properties.Delegates

@Suppress("DEPRECATION")
class MainPageActivity : AppCompatActivity() {
    private val frame: ConstraintLayout by lazy {
        findViewById(R.id.body_container)
    }
    private val bottomNagivationView: BottomNavigationView by lazy { // 하단 네비게이션 바
        findViewById(R.id.bottom_navigation)
    }

    private var memberId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // 애플리케이션 실행 후 첫 화면 설정
        supportFragmentManager.beginTransaction().replace(frame.id, GroupFragment()).commit()

        tokenCheck()

        getMyInfo()

        pushCheck()


        // 하단 네비게이션 바 클릭 이벤트 설정
        bottomNagivationView.setOnItemSelectedListener {item ->
            when(item.itemId) {
                /*R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }*/
                R.id.nav_group -> {
                    replaceFragment(GroupFragment())
                    tokenCheck()
                    true
                }
                R.id.nav_rent -> {
                    replaceFragment(RentFragment())
                    tokenCheck()
                    true
                }
                R.id.nav_mypage -> {
                    replaceFragment(MypageFragment())
                    tokenCheck()
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

    private fun pushCheck(){
        val fcmToken=MainActivity.GlobalApplication.prefs.getString("fcm","x")
        if(fcmToken!="x"){
            val push=MainActivity.GlobalApplication.prefs.getString("push","x")
            val init=MainActivity.GlobalApplication.prefs.getString("init","x")
            if(push=="x" && init=="x"){
                showDialogPush()
            }
        }
    }

    private fun tokenCheck(){
        val tokenCheck= MainActivity.GlobalApplication.prefs.getString("token","x")
        if(tokenCheck=="x"){
            finish()
        }
    }

    private fun getMyInfo(){
        RetrofitBuilder.api.myInfoRequest().enqueue(object :
            Callback<MyInfoModel> {
            override fun onResponse(
                call: Call<MyInfoModel>,
                response: Response<MyInfoModel>
            ) {
                if(response.isSuccessful) {
                    Log.d("testToken", response.body().toString())

                    val data=response.body()

                    memberId=data!!.data.id
                }
                else {
                    Log.d("fail", response.body().toString())
                }
            }

            override fun onFailure(call: Call<MyInfoModel>, t: Throwable) {
                Log.d("test", "실패$t")
            }

        })
    }

    private fun showDialogPush(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ren2U에서 알림을 보내고자 합니다.")
            .setMessage("앱 푸시 알림에 수신 혀용하시겠습니까?")
            .setPositiveButton("허용",
                DialogInterface.OnClickListener { dialog, id ->
                    val request=FcmModel(memberId=memberId, fcmToken = MainActivity.GlobalApplication.prefs.getString("fcm", "x"))

                    RetrofitBuilder.api.registerFCMToken(request).enqueue(object :
                        Callback<Void> {
                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {
                            if(response.isSuccessful) {
                                Log.d("testToken", response.code()!!.toString())
                            }
                            else {
                                Log.d("fail", response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.d("test", "실패$t")
                        }

                    })

                    MainActivity.GlobalApplication.prefs.setString("push","o")
                    MainActivity.GlobalApplication.prefs.getString("init","o")
                })
            .setNegativeButton("허용 안 함",
                DialogInterface.OnClickListener { dialog, id ->

                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    fun deleteToken(){
        MainActivity.GlobalApplication.prefs.removeString("token")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                replaceFragment(RentFragment())
            }
        }

        if(requestCode==2){
            if(resultCode== Activity.RESULT_OK){
                replaceFragment(RentFragment())
            }
        }
    }
}