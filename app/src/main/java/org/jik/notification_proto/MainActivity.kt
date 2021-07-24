package org.jik.notification_proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.adapter.CustomAdpater
import org.jik.notification_proto.data.Item
import org.jik.notification_proto.fragment.Fragment_Initial
import org.jik.notification_proto.fragment.Fragment_Selection
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val initial =Fragment_Initial()
        supportFragmentManager.beginTransaction().replace(R.id.main_content,initial).commit()

    }
}

