package com.ren2u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.model.RentDetail
import java.text.DecimalFormat

class MyRentalAdapter internal constructor(var productList: List<RentDetail>)
    : RecyclerView.Adapter<MyRentalAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: RentDetail) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            if(_list.imagePath!=null) {
                val newUrl=_list.imagePath
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            }
            itemView.findViewById<ImageView>(R.id.iv_image).clipToOutline=true

            val name=_list.name
            val df= DecimalFormat("000")
            val dn=df.format(_list.numbering)

            itemView.findViewById<TextView>(R.id.iv_name).text = "$name-$dn"
            itemView.findViewById<TextView>(R.id.iv_group).text = _list.clubName

            if(_list.rentalInfo==null){
                itemView.findViewById<Button>(R.id.iv_status).text = "대여가능"
            } else{
                    val status=_list.rentalInfo.rentalStatus
                    if(status=="WAIT"){
                        itemView.findViewById<Button>(R.id.iv_status).text = "예약중"
                    }
                    if(status=="RENT"){
                        itemView.findViewById<Button>(R.id.iv_status).text = "대여중"
                    }
                    if(status=="LATE"){
                        itemView.findViewById<Button>(R.id.iv_status).text = "연체"
                    }
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
            LayoutInflater.from(parent.context).inflate(R.layout.rental_list, parent, false)
        )
    }

    // 반환된 ViewHolder에 데이터를 연결한다.
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(productList[position])

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