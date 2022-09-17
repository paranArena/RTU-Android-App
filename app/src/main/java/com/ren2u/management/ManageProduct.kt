package com.ren2u.management

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.ren2u.R
import com.ren2u.databinding.ActivityManageProductBinding
import com.ren2u.management.product.ManageProductList
import com.ren2u.management.product.ManageRentFragment

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

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val id = getExtra()

        val bundle = Bundle()
        bundle.putInt("id", id)

        var fragmentManageRent=ManageRentFragment()
        var fragmentManageProductList=ManageProductList()

        fragmentManageRent.arguments = bundle
        fragmentManageProductList.arguments = bundle

        replaceFragment(fragmentManageRent)

        tabLayout = binding.tabLayout

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> replaceFragment(fragmentManageRent)
                    1 -> replaceFragment(fragmentManageProductList)
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