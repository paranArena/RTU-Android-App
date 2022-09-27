package com.ren2u.management.coupon

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ren2u.CustomDecoration
import com.ren2u.MainPageActivity
import com.ren2u.R
import com.ren2u.adapter.MemberListAdapter
import com.ren2u.adapter.MemberSelectAdapter
import com.ren2u.databinding.ActivitySelectMemberBinding
import com.ren2u.management.member.MemberInfoFragment
import com.ren2u.model.*
import com.ren2u.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectMemberActivity : AppCompatActivity() {
    private var _binding: ActivitySelectMemberBinding? = null

    val data_ = mutableListOf<MemberModel>()

    private val binding get() = _binding!!

    private lateinit var tabLayout: TabLayout
    private var selectedNumber=0
    private var allNumber=0

    private var selectedItemId: Array<Boolean> = Array(500) {false}

    private val members = mutableListOf<Int>()
    private val membersAll = mutableListOf<Int>()

    private fun getClubId(): Int {
        return intent.getIntExtra("clubId", 0)
    }

    private fun getCouponId(): Int {
        return intent.getIntExtra("couponId", 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_manage_detail)

        _binding = ActivitySelectMemberBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        binding.all.setOnClickListener {
            members.addAll(membersAll)
            selectedNumber=allNumber
            binding.selected.text="$selectedNumber 개 선택"

            for(i in 0 until allNumber){
                selectedItemId[i]=true
            }

            binding.rvList.adapter= MemberSelectAdapter(data_, true).apply {
                setItemClickListener(
                    object : MemberSelectAdapter.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            if (!selectedItemId[position]) {
                                selectedItemId[position] = true

                                members.add(memberList[position].id)

                                view.findViewById<ImageButton>(R.id.check)
                                    .setBackgroundResource(R.drawable.ic_check_button)
                                selectedNumber++
                                binding.selected.text = "$selectedNumber 개 선택"

                            } else {
                                selectedItemId[position] = false

                                members.remove(memberList[position].id)

                                view.findViewById<ImageButton>(R.id.check)
                                    .setBackgroundResource(R.drawable.solid_button)
                                selectedNumber--
                                binding.selected.text = "$selectedNumber 개 선택"
                            }
                        }
                    })
            }
        }

        binding.clear.setOnClickListener {
            members.clear()
            selectedNumber=0
            binding.selected.text="$selectedNumber 개 선택"

            for(i in 0 until allNumber){
                selectedItemId[i]=false
            }

            binding.rvList.adapter= MemberSelectAdapter(data_, false).apply {
                setItemClickListener(
                    object : MemberSelectAdapter.ItemClickListener {
                        override fun onClick(view: View, position: Int) {
                            if (!selectedItemId[position]) {
                                selectedItemId[position] = true

                                members.add(memberList[position].id)

                                view.findViewById<ImageButton>(R.id.check)
                                    .setBackgroundResource(R.drawable.ic_check_button)
                                selectedNumber++
                                binding.selected.text = "$selectedNumber 개 선택"

                            } else {
                                selectedItemId[position] = false

                                members.remove(memberList[position].id)

                                view.findViewById<ImageButton>(R.id.check)
                                    .setBackgroundResource(R.drawable.solid_button)
                                selectedNumber--
                                binding.selected.text = "$selectedNumber 개 선택"
                            }
                        }
                    })
            }
        }

        val clubId=getClubId()
        val couponId=getCouponId()

        binding.toolbarOption.setOnClickListener {
            grantCoupon(clubId, couponId)
        }

        initRecycler(clubId)

        val view = binding.root
        setContentView(view)
    }

    private fun grantCoupon(clubId: Int, couponId: Int){
        val request=CouponGrantRequest(memberIds = members)

        RetrofitBuilder.api.grantCouponAdmin(
           clubId, couponId, request
        ).enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SelectMemberActivity, "발급되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    when (response.code()) {
                        400 -> Log.d("test", response.body()!!.toString())
                    }
                    Log.d("test", response.body()!!.toString())
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Toast.makeText(this@SelectMemberActivity, "실패했습니다.", Toast.LENGTH_SHORT).show()
                Log.d("test", "실패$t")
                finish()
            }
        })
    }

    private fun initRecycler(id: Int) {

        binding.rvList.setDivider(3f,10f, this?.let {
            ContextCompat.getColor(
                it,
                R.color.lightGray
            )
        })

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

                    allNumber=0
                    for(item in data.data){
                        membersAll.add(item.id)
                        allNumber++
                    }

                    binding.rvList.adapter = MemberSelectAdapter(data_, false).apply {
                        setItemClickListener(
                            object : MemberSelectAdapter.ItemClickListener {
                                override fun onClick(view: View, position: Int) {
                                    if(!selectedItemId[position]){
                                        selectedItemId[position]=true

                                        members.add(memberList[position].id)

                                        view.findViewById<ImageButton>(R.id.check).setBackgroundResource(R.drawable.ic_check_button)
                                        selectedNumber++
                                        binding.selected.text="$selectedNumber 개 선택"

                                    } else{
                                        selectedItemId[position]=false

                                        members.remove(memberList[position].id)

                                        view.findViewById<ImageButton>(R.id.check).setBackgroundResource(R.drawable.solid_button)
                                        selectedNumber--
                                        binding.selected.text="$selectedNumber 개 선택"
                                    }
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

    fun RecyclerView.setDivider(dividerHeight: Float, dividerPadding: Float, @ColorInt dividerColor: Int?) {
        val decoration = CustomDecoration(
            height = dividerHeight ?: 0f,
            padding = dividerPadding ?: 0f,
            color = dividerColor ?: Color.TRANSPARENT
        )

        addItemDecoration(decoration)
    }
}