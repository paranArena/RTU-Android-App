package com.rtu.grouptap

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.MainPageActivity
import com.rtu.R
import com.rtu.databinding.FragmentGroupListBinding

class GroupListFragment : Fragment() {

    private var _binding: FragmentGroupListBinding?=null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)

        binding.addGroup.setOnClickListener {
            val intent = Intent(activity, AddGroup::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}