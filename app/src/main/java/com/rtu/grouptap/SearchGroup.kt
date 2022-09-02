package com.rtu.grouptap

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rtu.R
import com.rtu.adapter.GroupViewAdapter
import com.rtu.model.ClubSearchDetail
import com.rtu.model.GetSearchGroup
import com.rtu.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchGroup : AppCompatActivity() {
    private var mAdapter: GroupViewAdapter? = null
    //private var mNoticeContent: NoticeContent? = null
    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_group)
        initView()
    }

    private fun initView() {
        val recyclerView = findViewById<View>(R.id.rv_list) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val data_ = mutableListOf<ClubSearchDetail>()
        RetrofitBuilder.api.groupSearchRequest().enqueue(object :
            Callback<GetSearchGroup> {
            override fun onResponse(
                call: Call<GetSearchGroup>,
                response: Response<GetSearchGroup>
            ) {
                if (response.isSuccessful) {
                    data_.clear()
                    //Log.d("test", response.body().toString())
                    var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                    Log.d("test", data.toString())

                    for (item in data.data) {
                        data_.add(item)
                    }

                    mAdapter= GroupViewAdapter(data_)

                    recyclerView.adapter=mAdapter
                }

                else{
                    Log.d("test", "error")

                }
            }

            override fun onFailure(call: Call<GetSearchGroup>, t: Throwable) {
                Log.d("test", "실패$t")
                //Toast.makeText(getActivity(), "업로드 실패 ..", Toast.LENGTH_SHORT).show()
            }

        })
    }
}