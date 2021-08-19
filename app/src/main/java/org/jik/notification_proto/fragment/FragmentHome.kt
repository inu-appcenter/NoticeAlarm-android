package org.jik.notification_proto.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.HomeAdapter
import org.jik.notification_proto.keyword.KeywordDatabase


class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 초기화
        var keywordlist = mutableListOf<String>()

        val keyworddb = KeywordDatabase.getInstance(activity?.applicationContext!!)!!

        val runnable = Runnable {
            // keyword 들을 가져옴
            val keywords = keyworddb.keywordDAO().getAll()
            // 각 keyword 의 명들을 가져와서 list 에 추가
            for (i in keywords.indices){
                keywordlist.add(keywords[i].keyword)
            }
            val recyclerview= view.findViewById<RecyclerView>(R.id.home_recyclerview)
            // runnable(백그라운드 동작) 이라 runOnUiThread 로 감싸서 UI 를 건들이는 코드를 작성
            activity?.runOnUiThread {
                recyclerview.layoutManager = FlexboxLayoutManager(activity)
                recyclerview.adapter = HomeAdapter(keywordlist)
            }
        }

        val addThread = Thread(runnable)
        addThread.start()

        return view
    }
}