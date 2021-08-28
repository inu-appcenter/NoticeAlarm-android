package org.jik.notification_proto

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.adapter.NoticeListAdapter

class NoticeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        val prefs : SharedPreferences = this.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)!!
        val result = prefs.getString("장학","default")
        Log.d("받은 데이터",result.toString())
        var result_lst =result?.split(",")

        var notice_title_lst = mutableListOf<String>()
        for (i in 0..result_lst?.size!!-1){
            if (i % 3 == 1){
                notice_title_lst.add(result_lst[i])
            }
        }
        val recyclerview= this.findViewById<RecyclerView>(R.id.recyclerView_list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = NoticeListAdapter(notice_title_lst)
    }
}