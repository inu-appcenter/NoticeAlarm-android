package org.jik.notification_proto.adapter

import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.HomeActivity
import org.jik.notification_proto.NoticeActivity
import org.jik.notification_proto.R
import org.jik.notification_proto.SelectionActivity
import kotlin.math.roundToInt

class HomeAdapter(private val content:MutableList<String>) : RecyclerView.Adapter<HomeAdapter.Holder>() {
    // Int를 dp로 바꾸기 위해 필요한 요소를 초기화 하고 onCreateViewHolder에서 값 적용
    private  var displayMetrics : DisplayMetrics? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home,parent,false)
        displayMetrics = view.context.resources.displayMetrics
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val btn = holder.itemView.findViewById<Button>(R.id.home_btn)
        val param = btn.layoutParams as ViewGroup.MarginLayoutParams
        // 각 btn 의 색상을 position 에 맞게 변경
        if (position % 4 == 0 || (position-3) % 4 == 0) {
            btn.background = holder.itemView.resources.getDrawable(R.drawable.home_btn_yellow)
        }
        // Int 를 dp 로 바꾸는 함수
        fun changeDP(value : Int) : Int{
            return (value * displayMetrics!!.density).roundToInt()
        }
        if (position % 2 == 0){
            param.setMargins(changeDP(24),changeDP(14),0,0)
        }
        else{
            param.setMargins(changeDP(12),changeDP(14),changeDP(24),0)
        }
        holder.bind(content[position])

        btn.setOnClickListener {
            val notice_intent = Intent(holder.itemView.context, NoticeActivity::class.java)
            notice_intent.putExtra("keyword",btn.text)
            holder.itemView.context.startActivity(notice_intent)
        }
    }

    override fun getItemCount(): Int = content.size


    class Holder(View: View) : RecyclerView.ViewHolder(View){
        private val homebtn: TextView = itemView.findViewById(R.id.home_btn)
        fun bind(item: String){
            homebtn.text = item
        }
    }
}