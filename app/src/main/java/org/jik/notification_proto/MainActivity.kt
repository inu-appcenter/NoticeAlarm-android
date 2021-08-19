package org.jik.notification_proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jik.notification_proto.fragment.FragmentInitial

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val initial =FragmentInitial()
        supportFragmentManager.beginTransaction().replace(R.id.main_content,initial).commit()

//        val fragmentList = listOf(FragmentHome(),FragmentKeyword())
//        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
//        val adapter = FragmentAdapter(this)
//        adapter.fragmentList = fragmentList
//        viewPager.adapter = adapter

        //  college db에 학과가 있다면(학과가 이미 선택되어 있다면) Fragment_home 으로 만약 없으면 Fragment_initial 로 초기화면이 진행되게끔
//        var collegedb = CollegeDatabase.getInstance(applicationContext)!!
//        val r = Runnable {
//            val savedContacts = collegedb.collegeDAO().getAll()
//            if (savedContacts.isNotEmpty()) {
//                val home = FragmentHome()
//                supportFragmentManager.beginTransaction().replace(R.id.main_content,home).commit()
//            }
//            if (savedContacts.isEmpty()) {
//                val initial =FragmentInitial()
//                supportFragmentManager.beginTransaction().replace(R.id.main_content,initial).commit()
//
//            }
//        }
//        val addThread = Thread(r)
//        addThread.start()
//        val initial =FragmentInitial()
//        supportFragmentManager.beginTransaction().replace(R.id.main_content,initial).commit()

    }
}

