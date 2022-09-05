package com.rtu.management.product

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.tabs.TabLayout
import com.rtu.R
import com.rtu.databinding.FragmentManageProductListBinding
import com.rtu.grouptap.AddGroup
import com.rtu.management.product.add.AddProduct

class ManageProductList : Fragment() {
    private var _binding: FragmentManageProductListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentManageProductListBinding.inflate(inflater, container, false)

        val id=arguments?.getInt("id")

        binding.addProduct.setOnClickListener {
            val intent = Intent(activity, AddProduct::class.java)

            intent.apply {
                this.putExtra("id",id) // 데이터 넣기
            }

            startActivity(intent)
        }


        return binding.root
    }
}