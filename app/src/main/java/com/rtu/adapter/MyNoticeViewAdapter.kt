package com.rtu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rtu.R
import com.rtu.model.ClubSearchDetail
import com.rtu.model.NoticeDetailModel
import com.rtu.model.NoticeModel

class MyNoticeViewAdapter internal constructor(var noticeList: List<NoticeModel>)
    : RecyclerView.Adapter<MyNoticeViewAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: NoticeModel) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            /*if(_list.thumbnailPath!=null) {
                val newUrl=_list.thumbnailPath
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .override(60, 60).into(imageView)
            }*/
            itemView.findViewById<ImageView>(R.id.iv_image).clipToOutline=true

            itemView.findViewById<TextView>(R.id.iv_name).text = _list.title
            //itemView.findViewById<TextView>(R.id.iv_category).text = _list.category
            val createdAt=_list.createdAt.substring(0 until 10).replace("-",". ")

            itemView.findViewById<TextView>(R.id.iv_date).text = createdAt
        }
    }
    override fun getItemCount(): Int = noticeList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.my_notice_list, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(noticeList[position])

        /*val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 600
        holder.itemView.requestLayout()*/
    }

    interface ItemClickListener{
        fun onClick(view: View,position: Int)
    }
    //를릭 리스너
    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}