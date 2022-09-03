package com.rtu.grouptap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rtu.adapter.MyNoticeViewAdapter
import com.rtu.databinding.FragmentNoticeBinding
import com.rtu.model.GroupModel
import com.rtu.model.MyNotice
import com.rtu.model.NoticeModel
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeFragment : Fragment() {

    val data_ = mutableListOf<NoticeModel>()
    private var _binding: FragmentNoticeBinding?=null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)

        getAllNotice()

        return binding.root
    }

    private fun getAllNotice(){
        RetrofitBuilder.api.getMyClubNotice().enqueue(object :
            Callback<MyNotice> {
            override fun onResponse(

                call: Call<MyNotice>,
                response: Response<MyNotice>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    binding.rvList.adapter= MyNoticeViewAdapter(data_).apply{
                        setItemClickListener(
                            object : MyNoticeViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val clubId=noticeList[position].clubId
                                    val noticeId=noticeList[position].id

                                    //setFragmentResult("requestKey", bundleOf("projid" to projid))

                                    val intent = Intent(activity,
                                        NoticeInfo::class.java)

                                    intent.apply {
                                        this.putExtra("club_id",clubId)
                                        this.putExtra("notice_id",noticeId)// 데이터 넣기
                                    }
                                    startActivity(intent)
                                    //replaceFragment(GoodsInfoFragment())
                                }
                            })
                    }
                    //Toast.makeText(this@GoodsInfo, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MyNotice>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(this@GoodsInfo, "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}