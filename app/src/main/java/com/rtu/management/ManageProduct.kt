package com.rtu.management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.rtu.R
import com.rtu.databinding.ActivityManageProductBinding
import com.rtu.grouptap.GroupListFragment
import com.rtu.grouptap.NoticeFragment
import com.rtu.management.product.ManageProductList
import com.rtu.management.product.ManageRentFragment

class ManageProduct : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private var _binding: ActivityManageProductBinding? = null

    private val binding get() = _binding!!

    private fun getExtra(): Int {
        return intent.getIntExtra("id", 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

        _binding = ActivityManageProductBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        replaceFragment(ManageRentFragment())

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val id = getExtra()

        tabLayout = binding.tabLayout

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> replaceFragment(ManageRentFragment())
                    1 -> replaceFragment(ManageProductList())
                }
            }
        })


        val view = binding.root
        setContentView(view)
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
    }
}