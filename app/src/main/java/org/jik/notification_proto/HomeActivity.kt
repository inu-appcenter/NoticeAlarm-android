package org.jik.notification_proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.FragmentAdapter
import org.jik.notification_proto.fragment.FragmentHome
import org.jik.notification_proto.fragment.FragmentKeyword

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentList = listOf(FragmentHome(),FragmentKeyword())
        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
        val adapter = FragmentAdapter(this)
        adapter.fragmentList = fragmentList
        viewPager.adapter = adapter
    }
}