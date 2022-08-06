package com.rtu.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.tabs.TabLayout
import com.rtu.R
import com.rtu.databinding.FragmentGroupBinding
import com.rtu.grouptap.GroupListFragment
import com.rtu.grouptap.NoticeFragment

class GroupFragment : Fragment() {


    private var _binding: FragmentGroupBinding?=null

    private val binding get()=_binding!!

    private lateinit var tabLayout: TabLayout
    private lateinit var frameLayout: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGroupBinding.inflate(inflater, container, false)

        frameLayout = binding.groupList

        replaceFragment(GroupListFragment())

        tabLayout = binding.tabLayout

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> replaceFragment(GroupListFragment())
                    1 -> replaceFragment(NoticeFragment())
                }
            }
        })

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.group_list, fragment)
            .addToBackStack(null).commit()
    }

}