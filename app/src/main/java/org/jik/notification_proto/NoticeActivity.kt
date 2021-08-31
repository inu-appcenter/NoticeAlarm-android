package org.jik.notification_proto

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.adapter.NoticeListAdapter

class NoticeActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        // 키워드를 클릭하면 키워드 값을 토대로 local db에 존재하는 데이터 내용을 파싱해서 어답터에 패치
        val prefs : SharedPreferences = this.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)!!
        val keyword = intent.getStringExtra("keyword")
        val result = prefs.getString(keyword,"default")
        Log.d("받은 데이터",result.toString())
        var result_lst =result?.split(",")

        var notice_title_lst = mutableListOf<String>()
        for (i in 0..result_lst?.size!!-1){
            if (i % 4 == 1){
                notice_title_lst.add(result_lst[i])
            }
        }

        // 키워드 이름을 각 view 마다 따로 지정해서 바꿔줘야함
        findViewById<TextView>(R.id.keyword_title).text = "#$keyword"




        val recyclerview= this.findViewById<RecyclerView>(R.id.recyclerView_list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = NoticeListAdapter(notice_title_lst, keyword.toString())
    }
}