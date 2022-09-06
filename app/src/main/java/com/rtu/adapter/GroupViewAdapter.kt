package com.rtu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rtu.R
import com.rtu.model.ClubSearchDetail

class GroupViewAdapter internal constructor(var groupList: List<ClubSearchDetail>)
    : RecyclerView.Adapter<GroupViewAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: ClubSearchDetail) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            if(_list.thumbnailPath!=null) {
                val newUrl=_list.thumbnailPath
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .override(60, 60).into(imageView)
            }
            itemView.findViewById<ImageView>(R.id.iv_image).clipToOutline=true

            itemView.findViewById<TextView>(R.id.iv_name).text = _list.name
            //itemView.findViewById<TextView>(R.id.iv_category).text = _list.category
            var hashtags: String=""
            for(tag in _list.hashtags){
                hashtags += "#"
                hashtags += tag
                hashtags += " "
            }

            itemView.findViewById<TextView>(R.id.iv_tag).text = hashtags

            val role=_list.clubRole
            if(role=="NONE"){
                itemView.findViewById<Button>(R.id.join_button).text = "미가입"
            } else if(role=="OWNER" || role=="ADMIN"){
                itemView.findViewById<Button>(R.id.join_button).text = "관리자"
            } else if(role=="WAIT"){
                itemView.findViewById<Button>(R.id.join_button).text = "요청됨"
            } else if(role=="USER"){
                itemView.findViewById<Button>(R.id.join_button).text = "가입됨"
            }
        }
    }
    override fun getItemCount(): Int = groupList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.group_list, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(groupList[position])

        holder.itemView.setOnClickListener{
            itemClickListner.onClick(it,position)
        }

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