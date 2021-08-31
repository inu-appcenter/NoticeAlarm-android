package org.jik.notification_proto.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.DetailActivity
import org.jik.notification_proto.NoticeActivity
import org.jik.notification_proto.R
import org.jik.notification_proto.data.Notice

class NoticeListAdapter(private var notices:MutableList<String>, private var getkeyword:String) :RecyclerView.Adapter<NoticeListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(notices[position])
        holder.itemView.findViewById<ConstraintLayout>(R.id.item_constraintLayout).setOnClickListener {
            val detail_intent = Intent(holder.itemView.context, DetailActivity::class.java)
            detail_intent.putExtra("keyword",getkeyword)
            detail_intent.putExtra("title",holder.itemView.findViewById<TextView>(R.id.notice_title).text)
            holder.itemView.context.startActivity(detail_intent)
            holder.itemView.findViewById<ConstraintLayout>(R.id.constraintLayout)

        }
    }

    override fun getItemCount(): Int {
        return notices.size
    }

    class Holder(View: View) : RecyclerView.ViewHolder(View){
        private val notice_title: TextView = itemView.findViewById(R.id.notice_title)
        fun bind(notice:String){
            notice_title.text = notice
        }
    }
}