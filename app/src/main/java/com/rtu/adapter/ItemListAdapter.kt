package com.rtu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rtu.R
import com.rtu.model.ItemsModel
import java.text.DecimalFormat

class ItemListAdapter internal constructor(var itemList: List<ItemsModel>, val name: String)
    : RecyclerView.Adapter<ItemListAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: ItemsModel) {

            val df=DecimalFormat("000")
            val dn=df.format(_list.numbering)
            itemView.findViewById<TextView>(R.id.iv_name).text = "$name-$dn"

            if(_list.rentalInfo==null){
                itemView.findViewById<TextView>(R.id.iv_status).text = "대여가능"
            } else{
                if(_list.rentalInfo.meRental) {
                    val status=_list.rentalInfo.rentalStatus
                    if(status=="WAIT"){
                        itemView.findViewById<TextView>(R.id.iv_status).text = "예약중"
                    }
                    if(status=="RENT"){
                        itemView.findViewById<TextView>(R.id.iv_status).text = "대여중"
                    }
                    if(status=="LATE"){
                        itemView.findViewById<TextView>(R.id.iv_status).text = "연체"
                    }
                }
                else{
                    itemView.findViewById<TextView>(R.id.iv_status).text = "불가"
                }
            }
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