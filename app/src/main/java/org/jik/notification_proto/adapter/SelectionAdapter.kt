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
import org.jik.notification_proto.SelectionActivity
import org.jik.notification_proto.data.College
import org.jik.notification_proto.fragment.FragmentKeyword
import org.jik.notification_proto.fragment.FragmentSelection
import org.json.JSONObject
                                                            // 원래는 FragmentSelection.roomListAdapterToList 로 했었다
class SelectionAdapter(val colleges:MutableList<College>, var link: SelectionActivity.roomListAdapterToList):RecyclerView.Adapter<SelectionAdapter.Holder>() {
    var cnt = 0
    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_college, parent,false)
        return Holder(view)

    }

    override fun onBindViewHolder(holder: SelectionAdapter.Holder, position: Int) {
            holder.bind(colleges[position])
        // 선택이 되었을 때와 안 되었을 때 버튼의 background drawable 을  변경
        if (selectedPosition == position){
//            holder.itemView.findViewById<AppCompatButton>(R.id.college_btn).isSelected = true
            holder.itemView.findViewById<AppCompatButton>(R.id.college_btn).setBackgroundResource(R.drawable.depart_btn_select)

            val btntext = holder.itemView.findViewById<AppCompatButton>(R.id.college_btn).text.toString()
            link.getRoomId(btntext)

        }else{
//            holder.itemView.findViewById<AppCompatButton>(R.id.college_btn).isSelected = false
            holder.itemView.findViewById<AppCompatButton>(R.id.college_btn).setBackgroundResource(R.drawable.depart_btn)
        }

        // 단대 버튼이 클릭되면 json 파일을 가져와 위치가 동일한 (같은) 단대의 학과들을 colleges 에 추가시킴으로 view 업데이트
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
            // 두 번이상 클릭 == 학과 선택화면일 때를 의미
            if(cnt >= 2){
                //  선택된 버튼의 position 을 업데이트
                if (selectedPosition >= 0) {
                    notifyItemChanged(selectedPosition)
                }
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition)
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