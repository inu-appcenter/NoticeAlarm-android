package org.jik.notification_proto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.R
import org.jik.notification_proto.data.Item

class CustomAdpater(val datas:MutableList<Item>):RecyclerView.Adapter<CustomAdpater.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdpater.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: CustomAdpater.Holder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size

    class Holder(View: View) : RecyclerView.ViewHolder(View){
        private val title: TextView = itemView.findViewById(R.id.title)
        private val date: TextView = itemView.findViewById(R.id.date)
        fun bind(item: Item){
            title.text = item.title
            date.text = item.date
        }
    }
}
