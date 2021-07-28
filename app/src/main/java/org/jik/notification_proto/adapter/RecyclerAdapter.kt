package org.jik.notification_proto.adapter

import android.content.res.AssetManager
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
import org.json.JSONObject
import kotlin.collections.listOf as listOf1

class RecyclerAdapter(val colleges:MutableList<College>):RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var cnt = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_college, parent,false)
        return Holder(view)

    }

    override fun onBindViewHolder(holder: RecyclerAdapter.Holder, position: Int) {
        holder.bind(colleges[position])
        // 단대 버튼이 클릭되면 json파일을 가져와 위치가 동일한 (같은) 단대의 학과들을 colleges에 추가시킴으로 view 업데이트
        holder.itemView.findViewById<AppCompatButton>(R.id.college_btn).setOnClickListener {
            cnt += 1
            if (cnt < 2) {
                colleges.clear()
                val assetManager: AssetManager = holder.itemView.resources?.assets!!
                val inputStream = assetManager.open("json/INU.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val jObject = JSONObject(jsonString)
                val jsonArray = jObject.getJSONArray("INU")
                for (i in 0 until jsonArray.length()) {
                    if (i == holder.adapterPosition) {
                        val iObject = jsonArray.getJSONObject(i)
                        val major = iObject.getJSONArray("major")
                        for (j in 0 until major.length()) {
                            colleges.add(College(major[j] as String))
                        }
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = colleges.size

    class Holder(View: View) : RecyclerView.ViewHolder(View){
        private var college_btn:AppCompatButton = itemView.findViewById(R.id.college_btn)
        fun bind(college:College){
            college_btn.text = college.name
        }
    }
}