package org.jik.notification_proto.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.RecyclerAdapter
import org.jik.notification_proto.data.College

class FragmentSelection : Fragment() {
    private val list = listOf("인문대학","자연과학대학","사회과학대학","글로벌정경대학","공과대학","정보기술대학","경영대학","예술체육대학")
    lateinit var recyclerview : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment__selection, container, false)
        val collegelist :MutableList<College> = mutableListOf()

        for(i in list){
            collegelist.add(College(i))
        }

        recyclerview = view.findViewById(R.id.selection_recyclerView) as RecyclerView
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = RecyclerAdapter(collegelist)

        val keyword = FragmentKeyword()
        view.findViewById<AppCompatButton>(R.id.select_btn).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_content,keyword).addToBackStack(null).commit()
        }

        return view
    }
}