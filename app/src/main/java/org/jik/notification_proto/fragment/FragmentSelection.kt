package org.jik.notification_proto.fragment

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.RecyclerAdapter
import org.jik.notification_proto.data.College
import org.json.JSONObject

class FragmentSelection : Fragment() {
    lateinit var recyclerview : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment__selection, container, false)
        val collegelist :MutableList<College> = mutableListOf()

        // json 파일 가져와서 collegelist에 추가시켜주기
        val assetManager: AssetManager = context?.resources?.assets!!
        val inputStream= assetManager.open("json/INU.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jObject = JSONObject(jsonString)
        val jsonArray = jObject.getJSONArray("INU")
        for (i in 0 until jsonArray.length()) {
            val iObject = jsonArray.getJSONObject(i)
            val college = iObject.getString("college")
            collegelist.add(College(college))
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