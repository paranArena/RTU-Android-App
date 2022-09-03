package com.rtu.management.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.tabs.TabLayout
import com.rtu.R
import com.rtu.databinding.FragmentManageRentBinding

class ManageRentFragment : Fragment() {
    private var _binding: FragmentManageRentBinding? = null

    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var frameLayout: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentManageRentBinding.inflate(inflater, container, false)
        replaceFragment(ManageBookList())

        frameLayout = binding.frame
        tabLayout = binding.tabLayout

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> replaceFragment(ManageBookList())
                    1 -> replaceFragment(ManageBookList())
                    2 -> replaceFragment(ManageBookList())
                }
            }
        })

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.frame, fragment)
            .addToBackStack(null).commit()
    }
}