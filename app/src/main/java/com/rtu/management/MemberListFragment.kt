package com.rtu.management

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rtu.adapter.MemberListAdapter

import com.rtu.databinding.FragmentMemberListBinding
import com.rtu.model.MemberListModel
import com.rtu.model.MemberModel
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberListFragment : Fragment() {
    private var _binding: FragmentMemberListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<MemberModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemberListBinding.inflate(inflater, container, false)

        //Log.d("test", "before")
        /*binding.addGroup.setOnClickListener {
            val intent = Intent(activity, AddGroup::class.java)
            startActivity(intent)
        }*/

        val id= arguments?.getInt("id", 1)

        initRecycler(id!!)

        return binding.root
    }

    private fun initRecycler(id: Int) {
        RetrofitBuilder.api.getAllMember(id).enqueue(object :
            Callback<MemberListModel> {
            override fun onResponse(
                call: Call<MemberListModel>,
                response: Response<MemberListModel>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter = MemberListAdapter(data_).apply {
                        setItemClickListener(
                            object : MemberListAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val id = memberList[position].id

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    /*val intent = Intent(context, GroupInfo::class.java)

                                    intent.apply {
                                        this.putExtra("id", id) // 데이터 넣기
                                    }
                                    startActivity(intent)*/

                                    //replaceFragment(GoodsInfoFragment())
                                }
                            })
                    }
                } else {
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<MemberListModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}