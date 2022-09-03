package com.rtu.management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.rtu.R
import com.rtu.databinding.ActivityManageMemberBinding
import com.rtu.grouptap.NoticeFragment
import kotlinx.android.synthetic.main.activity_manage_member.*

class ManageMember : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var frameLayout: FrameLayout
    private var _binding: ActivityManageMemberBinding?=null

    private val binding get() = _binding!!

    private fun getExtra(): Int {
        return intent.getIntExtra("id", 0)
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
        setContentView(R.layout.activity_manage_notice)

        _binding= ActivityManageMemberBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val id=getExtra()

        tabLayout = binding.tabLayout

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> replaceFragment(MemberListFragment())
                    1 -> replaceFragment(NoticeFragment())
                }
            }
        })


        val view=binding.root
        setContentView(view)
    }
    private fun replaceFragment(fragment: Fragment) {
        fun replaceFragment(fragment: Fragment) {
            supportFragmentManager.beginTransaction().replace(frame.id, fragment).commit()
        }
    }
}