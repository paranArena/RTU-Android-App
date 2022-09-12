package com.ren2u.grouptap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ren2u.R
import com.ren2u.adapter.GroupViewAdapter
import com.ren2u.model.ClubSearchDetail
import com.ren2u.model.GetSearchGroup
import com.ren2u.retrofit.RetrofitBuilder
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

        val noticeSearchView=findViewById<SearchView>(R.id.notice_search_view)

        noticeSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 누를 때 호출
                searchName(query)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun searchName(query: String?){
        if(query==null || query==""){
            initView()
        } else{
            val recyclerView = findViewById<View>(R.id.rv_list) as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)

            val data_ = mutableListOf<ClubSearchDetail>()
            RetrofitBuilder.api.groupSearchNameRequest(query).enqueue(object :
                Callback<GetSearchGroup> {
                override fun onResponse(
                    call: Call<GetSearchGroup>,
                    response: Response<GetSearchGroup>
                ) {
                    if (response.isSuccessful) {
                        data_.clear()
                        //Log.d("test", response.body().toString())
                        var data = response.body()!! // GsonConverter를 사용해 데이터매핑
                        Log.d("test", query + data.toString())

                        for (item in data.data) {
                            data_.add(item)
                        }

                        mAdapter= GroupViewAdapter(data_)

                        recyclerView.adapter= mAdapter?.apply{
                            setItemClickListener(
                                object : GroupViewAdapter.ItemClickListener {
                                    override fun onClick(view: View, position: Int) {
                                        val id=groupList[position].id

                                        val intent = Intent(this@SearchGroup,
                                            GroupInfo::class.java)

                                        intent.apply {
                                            this.putExtra("id",id) // 데이터 넣기
                                        }
                                        startActivity(intent)

                                    }
                                })
                        }
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

                    recyclerView.adapter= mAdapter?.apply{
                        setItemClickListener(
                            object : GroupViewAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    val id=groupList[position].id

                                    val intent = Intent(this@SearchGroup,
                                        GroupInfo::class.java)

                                    intent.apply {
                                        this.putExtra("id",id) // 데이터 넣기
                                    }
                                    startActivity(intent)

                                }
                            })
                    }
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