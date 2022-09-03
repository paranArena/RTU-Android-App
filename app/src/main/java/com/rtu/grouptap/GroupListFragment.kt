package com.rtu.grouptap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.adapter.GroupViewAdapter
import com.rtu.adapter.MyGroupViewAdapter
import com.rtu.databinding.FragmentGroupListBinding
import com.rtu.model.*
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupListFragment : Fragment() {

    private var _binding: FragmentGroupListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<ClubInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)

        //Log.d("test", "before")
        binding.addGroup.setOnClickListener {
            val intent = Intent(activity, AddGroup::class.java)
            startActivity(intent)
        }

        initRecycler()

        return binding.root
    }

    private fun initRecycler(){
        RetrofitBuilder.api.myGroupRequest().enqueue(object :
                Callback<GetGroupModel> {
                override fun onResponse(
                    call: Call<GetGroupModel>,
                    response: Response<GetGroupModel>
                ) {
                    if (response.isSuccessful) {
                        data_.clear()
                        //Log.d("test", response.body().toString())
                        var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                        Log.d("test", data.toString())

                        for (item in data.data) {
                            data_.add(item)
                        }

                        binding.rvList.adapter= MyGroupViewAdapter(data_).apply{
                            setItemClickListener(
                                object : MyGroupViewAdapter.ItemClickListener {
                                    override fun onClick(view: View, position: Int) {
                                        val id=groupList[position].id

                                        //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                        val intent = Intent(context,GroupInfo::class.java)

                                        intent.apply {
                                            this.putExtra("id",id) // 데이터 넣기
                                        }
                                        startActivity(intent)

                                        //replaceFragment(GoodsInfoFragment())
                                    }
                                })
                        }
                    }

                    else{
                        Log.d("test", "error")

                    }
                }

                override fun onFailure(call: Call<GetGroupModel>, t: Throwable) {
                    Log.d("test", "실패$t")
                    //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })


        /*data.apply {
            RetrofitBuilder.api.groupGetRequest().enqueue(object :
                Callback<GetGroupModel> {
                override fun onResponse(
                    call: Call<GetGroupModel>,
                    response: Response<GetGroupModel>
                ) {
                    if (response.isSuccessful) {
                        clear()
                        //Log.d("test", response.body().toString())
                        var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                        Log.d("test", data.toString())

                        for (item in data.data) {
                            add(item.club)
                        }
                    }
                }

                override fun onFailure(call: Call<GetGroupModel>, t: Throwable) {
                    Log.d("test", "실패$t")
                    //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
                }

            })

            groupViewAdapter.groupList = data
            groupViewAdapter.notifyDataSetChanged()
        }*/

    }
}