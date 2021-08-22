package org.jik.notification_proto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayout
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.FragmentAdapter
import org.jik.notification_proto.adapter.HomeAdapter
import org.jik.notification_proto.fragment.FragmentHome
import org.jik.notification_proto.fragment.FragmentKeyword
import org.jik.notification_proto.fragment.FragmentSelection
import org.jik.notification_proto.keyword.KeywordDatabase

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentList = listOf(FragmentHome(), FragmentKeyword())
        val viewPager = findViewById<ViewPager2>(R.id.viewpager)
        val adapter = FragmentAdapter(this)
        adapter.fragmentList = fragmentList
        viewPager.adapter = adapter

        // 여기서부터가 8/21일 새벽에 하던것들
        // 보류 페이지가 넘어가면서 동작을 추가하는 구문인데 이게 맞나??
        // 문제는 키워드 목록에서 키워드를 삭제하거나 추가하면 드래그해서 홈화면에서도 그 키워드로 view 가 적용이 되길 원함

//        settings 버튼 처리 Selection Activity 로 가게 해놓음
        findViewById<AppCompatButton>(R.id.setting_btn).setOnClickListener {
            val intent_selection = Intent(this, SelectionActivity::class.java)
            intent_selection.putExtra("what_activity","home")
            startActivity(intent_selection)
        }
//            val selection = FragmentSelection()
//            supportFragmentManager.beginTransaction().replace(R.id.Frame,selection).commit()
//
//            viewPager.visibility = View.GONE
//        }

        val pageCallBack = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // position 을 1로 하면 데이터 갱신에서 충돌이 생겨 원하는대로 안됨 그래서 0에서 해야 함
                if (position == 0) {
                    var keywordlist = mutableListOf<String>()
                    val keyworddb = KeywordDatabase.getInstance(applicationContext)!!
                    val runnable = Runnable {
                        // keyword 들을 가져옴
                        val keywords = keyworddb.keywordDAO().getAll()
                        // 각 keyword 의 명들을 가져와서 list 에 추가
                        for (i in keywords.indices) {
                            keywordlist.add(keywords[i].keyword)
                        }
                        // 새로고침
                        val recyclerview = findViewById<RecyclerView>(R.id.home_recyclerview)
                        runOnUiThread {
                            recyclerview.layoutManager = FlexboxLayoutManager(viewPager.context)
                            recyclerview.adapter = HomeAdapter(keywordlist)
                        }
//                    HomeAdapter(keywordlist).notifyDataSetChanged()
//                    val ft : FragmentTransaction = supportFragmentManager.beginTransaction()
//                    ft.detach(FragmentHome()).attach(FragmentHome()).commit()
                    }
                    val addThread = Thread(runnable)
                    addThread.start()
                }
            }
        }
            viewPager.registerOnPageChangeCallback(pageCallBack)

    }
}