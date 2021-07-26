package org.jik.notification_proto.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.R
import org.jik.notification_proto.data.College
import org.jik.notification_proto.fragment.FragmentSelection

class RecyclerAdapter(val colleges:MutableList<College>):RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_college, parent,false)
        var college_btn:AppCompatButton = view.findViewById(R.id.college_btn)
        college_btn.setOnClickListener {
            colleges.clear()
            if (college_btn.text == "정보기술대학"){
                colleges.add(College("컴퓨터공학부"))
                colleges.add(College("정보통신공학과"))
                colleges.add(College("임베디드시스템공학과"))
            }
            notifyDataSetChanged()
        }

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