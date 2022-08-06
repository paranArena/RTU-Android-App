package com.rtu.grouptap

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.adapter.GroupViewAdapter
import com.rtu.databinding.FragmentGroupListBinding
import com.rtu.model.GroupModel

class GroupListFragment : Fragment() {

    private var _binding: FragmentGroupListBinding?=null

    private val binding get() = _binding!!

    lateinit var groupViewAdapter: GroupViewAdapter
    val data = mutableListOf<GroupModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)

        binding.addGroup.setOnClickListener {
            val intent = Intent(activity, AddGroup::class.java)
            startActivity(intent)
        }

        initRecycler()

        return binding.root
    }

    private fun initRecycler(){

        groupViewAdapter = GroupViewAdapter(data)
        binding.rvList.adapter = groupViewAdapter


        data.apply {
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))

            groupViewAdapter.groupList = data
            groupViewAdapter.notifyDataSetChanged()

        }

    }
}