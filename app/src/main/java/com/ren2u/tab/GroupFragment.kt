package com.ren2u.tab

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.tabs.TabLayout
import com.ren2u.grouptap.GroupListFragment
import com.ren2u.grouptap.NoticeFragment
import com.ren2u.grouptap.SearchGroup
import com.ren2u.R
import com.ren2u.databinding.FragmentGroupBinding

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


        binding.searchView.setOnClickListener {
            val intent = Intent(activity, SearchGroup::class.java)
            startActivity(intent)
        }


        /*binding.searchView.setOnSearchClickListener{

        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                // 검색 버튼 누를 때 호출

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })*/

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