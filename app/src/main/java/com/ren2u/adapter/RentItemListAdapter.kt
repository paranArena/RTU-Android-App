package com.ren2u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ren2u.R
import com.ren2u.model.ItemsModel
import java.text.DecimalFormat

class RentItemListAdapter internal constructor(var itemList: List<ItemsModel>, val name: String)
    : RecyclerView.Adapter<RentItemListAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: ItemsModel) {

            val df= DecimalFormat("000")
            val dn=df.format(_list.numbering)
            itemView.findViewById<TextView>(R.id.iv_name).text = "$name-$dn"

            /*val status: String?=_list.rentalInfo.rentalStatus

            if(status==null){
                itemView.findViewById<TextView>(R.id.iv_status).text = "대여가능"
            } else {
                itemView.findViewById<TextView>(R.id.iv_status).text = _list.rentalInfo.rentalStatus
            }*/
        }
    }
    override fun getItemCount(): Int = itemList.size


    override fun getItemViewType(position: Int): Int {
        return position
    }

    // ViewHolder를 생성해 반환한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            // 새로운 뷰를 생성해 뷰홀더에 인자로 넣어준다.
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.itemView.setOnClickListener{
            itemClickListner.onClick(it,position)
        }


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