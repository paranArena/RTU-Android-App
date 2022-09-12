package com.ren2u.management

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ren2u.adapter.MemberListAdapter

import com.ren2u.databinding.FragmentMemberListBinding
import com.ren2u.management.member.MemberInfoFragment
import com.ren2u.model.MemberListModel
import com.ren2u.model.MemberModel
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberListFragment : Fragment() {
    private var _binding: FragmentMemberListBinding?=null

    private val binding get() = _binding!!

    //lateinit var groupViewAdapter: GroupViewAdapter
    val data_ = mutableListOf<MemberModel>()

    override fun onResume() {
        val id= arguments?.getInt("id", 1)
        initRecycler(id!!)
        super.onResume()
    }

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
                                    val email = memberList[position].email
                                    val number = memberList[position].phoneNumber
                                    val major = memberList[position].major
                                    val name = memberList[position].name
                                    val clubRole = memberList[position].clubRole
                                    val studentId = memberList[position].studentId
                                    val memberId = memberList[position].id

                                    val dialog=MemberInfoFragment()

                                    val bundle=Bundle()

                                    bundle.putString("email", email)
                                    bundle.putString("number", number)
                                    bundle.putString("major", major)
                                    bundle.putString("name", name)
                                    bundle.putString("clubRole", clubRole)
                                    bundle.putString("studentId", studentId)
                                    bundle.putInt("memberId", memberId)
                                    bundle.putInt("clubId", id)
                                    bundle.putInt("position", position)
                                    dialog.arguments=bundle

                                    dialog.show(childFragmentManager, "MemberInfoFragment")

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
    fun removeItem(position: Int){
        binding.rvList.adapter = MemberListAdapter(data_).apply {
            data_.removeAt(position)

            notifyDataSetChanged()
        }
    }
}