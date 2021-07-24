package org.jik.notification_proto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.R
import org.jik.notification_proto.data.College

class RecyclerAdapter(val colleges:MutableList<College>):RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_college, parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.Holder, position: Int) {
        holder.bind(colleges[position])
    }

    override fun getItemCount(): Int = colleges.size

    class Holder(View: View) : RecyclerView.ViewHolder(View){
        private var college_btn:AppCompatButton = itemView.findViewById(R.id.college_btn)
        fun bind(college:College){
            college_btn.text = college.name
        }
    }
}