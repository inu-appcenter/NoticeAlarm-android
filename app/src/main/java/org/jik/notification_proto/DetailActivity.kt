package org.jik.notification_proto

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var date : String?= null
        var content : String?= null


//        val keyword = intent.getStringExtra("keyword")
//        findViewById<TextView>(R.id.detail_keyword_title).text = keyword

        val title = intent.getStringExtra("title")
        findViewById<TextView>(R.id.detail_title).text = title
        val prefs : SharedPreferences = this.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)!!
        val keyword = intent.getStringExtra("keyword")
        val result = prefs.getString(keyword,"default")
        var result_lst =result?.split(",")
        Log.d("리스트",keyword.toString())
        for (i in 0 until result_lst?.size!!){
            Log.d("리스트의 각 요소",result_lst[i])
            if (result_lst[i] == title){
                content = result_lst[i+1]
                date = result_lst[i+2]
                Log.d("indate",date.toString())
                Log.d("incontent",content.toString())
            }
        }
        Log.d("date",date.toString())
        Log.d("content",content.toString())
        findViewById<TextView>(R.id.detail_date).text = date
        findViewById<TextView>(R.id.detail_content).text = content
        findViewById<TextView>(R.id.detail_keyword_title).text = "#$keyword"
    }
}