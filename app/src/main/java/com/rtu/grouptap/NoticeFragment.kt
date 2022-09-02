package com.rtu.grouptap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.adapter.MyGroupViewAdapter
import com.rtu.databinding.FragmentNoticeBinding
import com.rtu.model.GroupModel

class NoticeFragment : Fragment() {

    private var _binding: FragmentNoticeBinding?=null

    private val binding get() = _binding!!

    lateinit var groupViewAdapter: MyGroupViewAdapter
    val data = mutableListOf<GroupModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler(){

        /*groupViewAdapter = GroupViewAdapter(data)
        binding.rvList.adapter = groupViewAdapter


        data.apply {
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))
            add(GroupModel(name = "그룹이름", introduction = "#태그 #태그"))

            groupViewAdapter.groupList = data
            groupViewAdapter.notifyDataSetChanged()

        }*/

    }
}