package com.ren2u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ren2u.R
import com.ren2u.model.CouponMemberModel
import com.ren2u.model.MemberModel

class UnusedCouponAdapter internal constructor(var memberList: List<CouponMemberModel>)
    : RecyclerView.Adapter<UnusedCouponAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: CouponMemberModel) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            /*if(_list.thumbnailPath!=null) {
                val newUrl=_list.thumbnailPath
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .override(60, 60).into(imageView)
            }*/
            //Glide.with(itemView).load(R.drawable.ic_baseline_account_circle_24).placeholder(R.drawable.ic_launcher_foreground)
            //   .override(60, 60).into(imageView)


            itemView.findViewById<ImageView>(R.id.iv_image).clipToOutline=true

            itemView.findViewById<TextView>(R.id.iv_name).text = _list.memberPreviewDto.name
            //itemView.findViewById<TextView>(R.id.iv_category).text = _list.category
            itemView.findViewById<TextView>(R.id.iv_tag).text =
                (_list.memberPreviewDto.major + " " + _list.memberPreviewDto.studentId.substring(2 until 4))
        }
    }
    override fun getItemCount(): Int = memberList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.member_list, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(memberList[position])


        /*val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 600
        holder.itemView.requestLayout()*/
    }

    interface ItemClickListener{
        fun onClick(view: View, position: Int)
    }
    //를릭 리스너
    private lateinit var itemClickListner: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

}