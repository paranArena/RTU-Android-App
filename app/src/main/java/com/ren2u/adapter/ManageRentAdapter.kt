package com.ren2u.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ren2u.R
import com.ren2u.model.ManageRentData

class ManageRentAdapter internal constructor(var productList: List<ManageRentData>)
    : RecyclerView.Adapter<ManageRentAdapter.ListViewHolder>() {


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(_list: ManageRentData) {
            val imageView: ImageView = itemView.findViewById<ImageView>(R.id.iv_image)
            if(_list.imagePath!=null) {
                val newUrl=_list.imagePath
                Glide.with(itemView).load(newUrl).placeholder(R.drawable.ic_launcher_foreground)
                    .into(imageView)
            }
            itemView.findViewById<ImageView>(R.id.iv_image).clipToOutline=true

            itemView.findViewById<TextView>(R.id.iv_product).text = _list.name
            itemView.findViewById<TextView>(R.id.iv_name).text=_list.memberName

            val rentDate=_list.rentalInfo.rentDate.substring(0 until 10).replace("-",". ")

            itemView.findViewById<TextView>(R.id.iv_log).text = rentDate

            if(_list.rentalInfo.rentalStatus=="WAIT"){
                itemView.findViewById<TextView>(R.id.iv_explain).text = "픽업 예정"
                itemView.findViewById<TextView>(R.id.iv_date).text = ""
            } else{
                itemView.findViewById<TextView>(R.id.iv_explain).text = "반납 예정"
                val dueDate=_list.rentalInfo.expDate!!.substring(0 until 10).replace("-",". ")
                itemView.findViewById<TextView>(R.id.iv_date).text = dueDate
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
            LayoutInflater.from(parent.context).inflate(R.layout.return_list, parent, false)
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