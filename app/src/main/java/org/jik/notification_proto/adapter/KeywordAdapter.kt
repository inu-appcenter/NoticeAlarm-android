package org.jik.notification_proto.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.FireBaseMessagingService
import org.jik.notification_proto.HomeActivity
import org.jik.notification_proto.MainActivity
import org.jik.notification_proto.R
import org.jik.notification_proto.api.APIS
import org.jik.notification_proto.api.DeleteModel
import org.jik.notification_proto.fragment.FragmentKeyword
import org.jik.notification_proto.keyword.KeywordEntity
import org.jik.notification_proto.keyword.OnDeleteListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KeywordAdapter(var keywords: List<KeywordEntity>,var onDeleteListener: OnDeleteListener) :RecyclerView.Adapter<KeywordAdapter.Holder>(){
    private var token: String?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_keyword,parent,false)
        token = view.context.getSharedPreferences("token", Context.MODE_PRIVATE)?.getString("token","default value")
        return Holder(view)
    }



    override fun onBindViewHolder(holder: Holder, position: Int) {
        val keyword = keywords[position]

        holder.keywordname.text = keyword.keyword
        holder.keyworddelete.setOnClickListener {


            // 키워드 삭제 내용을 서버로 전달
            // val token = this.getContext().getSharedPreferences("token", Context.MODE_PRIVATE)?.getString("token","default value")
            var deletedata = DeleteModel(token, keyword = keyword.keyword)
            Log.d("deletedata", deletedata.toString())
            APIS.create().delete_users(deletedata).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())
                    // 서버응답이 200 일 때(네트워크가 연결되었을 때, 서버가 켜져있을 때)
                    onDeleteListener.onDeleteListener(keyword)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("log", t.printStackTrace().toString())
                    Log.d("log", "fail")
                    Toast.makeText(holder.itemView.context,"네트워크 연결을 확인 해주세요! ", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int  = keywords.size

    class Holder(View: View) :RecyclerView.ViewHolder(View){
        val keywordname = itemView.findViewById<TextView>(R.id.keyword_name)
        val keyworddelete = itemView.findViewById<TextView>(R.id.keyword_delete)
    }

}