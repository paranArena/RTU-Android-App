package com.ren2u.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ren2u.adapter.JoinListAdapter
import ren2u.databinding.FragmentMemberJoinBinding

import com.ren2u.model.MemberListModel
import com.ren2u.model.MemberModel
import com.ren2u.model.ResponseModel
import com.ren2u.retrofit.RetrofitBuilder
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberJoinFragment : Fragment() {
    private var _binding: FragmentMemberJoinBinding? = null

    private val binding get() = _binding!!
    private var data_ = mutableListOf<MemberModel>()



    //lateinit var groupViewAdapter: GroupViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemberJoinBinding.inflate(inflater, container, false)

        //Log.d("test", "before")
        /*binding.addGroup.setOnClickListener {
            val intent = Intent(activity, AddGroup::class.java)
            startActivity(intent)
        }*/

        val clubId = arguments?.getInt("id", 1)


        GlobalScope.launch {
            delay(1000)

            activity?.runOnUiThread {
                binding.rvList.adapter = JoinListAdapter(data_).apply {
                    setItemClickListener(
                        object : JoinListAdapter.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                val id = memberList[position].id

                                getAccept(clubId!!, id)

                                data_.removeAt(position)

                                notifyDataSetChanged()
                            }
                        })
                }
            }
        }

        initRecycler(clubId!!)

        return binding.root
    }

    private fun initRecycler(id: Int) {
        RetrofitBuilder.api.getJoinRequest(id).enqueue(object :
            Callback<MemberListModel> {
            override fun onResponse(
                call: Call<MemberListModel>,
                response: Response<MemberListModel>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    /*binding.rvList.adapter = JoinListAdapter(data_).apply {
                        setItemClickListener(
                            object : JoinListAdapter.ItemClickListener {
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
                    }*/
                } else {
                    Log.d("test", response.code().toString())

                }
            }

            override fun onFailure(call: Call<MemberListModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getAccept(clubId: Int, studentId: Int) {
        RetrofitBuilder.api.getAcceptRequest(clubId, studentId).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(

                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!
                }
                Log.d("test", response.code().toString())
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }

}