package org.jik.notification_proto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.R
import org.jik.notification_proto.keyword.KeywordEntity
import org.jik.notification_proto.keyword.OnDeleteListener

class KeywordAdapter(var keywords: List<KeywordEntity>,var onDeleteListener: OnDeleteListener) :RecyclerView.Adapter<KeywordAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_keyword,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val keyword = keywords[position]

        holder.keywordname.text = keyword.keyword
        holder.keyworddelete.setOnClickListener {
            onDeleteListener.onDeleteListener(keyword)
        }


    }

    override fun getItemCount(): Int  = keywords.size

    class Holder(View: View) :RecyclerView.ViewHolder(View){
        val keywordname = itemView.findViewById<TextView>(R.id.keyword_name)
        val keyworddelete = itemView.findViewById<TextView>(R.id.keyword_delete)
    }

}