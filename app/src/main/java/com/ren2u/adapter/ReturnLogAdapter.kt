package com.ren2u.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.model.ReturnLog

class ReturnLogAdapter internal constructor(var productList: List<ReturnLog>)
    : RecyclerView.Adapter<ReturnLogAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: ReturnLog) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            if(_list.thumbnailPath!=null) {
                val newUrl=_list.thumbnailPath
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .override(60, 60).into(imageView)
            }
            itemView.findViewById<ImageView>(R.id.iv_image).clipToOutline=true

            itemView.findViewById<TextView>(R.id.iv_product).text = _list.productName
            itemView.findViewById<TextView>(R.id.iv_name).text=_list.memberName

            val rentDate=_list.rentDate.substring(0 until 10).replace("-",". ")
            if(_list.expDate!=null) {
                val expDate = _list.expDate.substring(0 until 10).replace("-", ". ")
                itemView.findViewById<TextView>(R.id.iv_log).text = "$rentDate ~ $expDate"
            } else{
                val expDate=""
                itemView.findViewById<TextView>(R.id.iv_log).text = "$rentDate"
            }
            val returnDate=_list.returnDate.substring(0 until 10).replace("-",". ")



            itemView.findViewById<TextView>(R.id.iv_date).text = returnDate

            if(_list.rentalStatus=="DONE"){
                itemView.findViewById<TextView>(R.id.iv_explain).text = "기한 내 반납"
                itemView.findViewById<TextView>(R.id.iv_explain).setTextColor(Color.BLUE)
            }

            if(_list.rentalStatus=="LATE"){
                itemView.findViewById<TextView>(R.id.iv_explain).text = "늦음"
                itemView.findViewById<TextView>(R.id.iv_explain).setTextColor(Color.RED)
            }

            if(_list.rentalStatus=="CANCEL"){
                itemView.findViewById<TextView>(R.id.iv_explain).text = "예약 취소"
            }
            //itemView.findViewById<TextView>(R.id.iv_category).text = _list.category

        }
    }
    override fun getItemCount(): Int = productList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.manage_reserve_list, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(productList[position])

        /*holder.itemView.setOnClickListener{
            itemClickListner.onClick(it,position)
        }*/

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