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
import com.github.kimcore.inko.Inko
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.SelectionAdapter
import org.jik.notification_proto.data.College
import org.json.JSONObject

class FragmentSelection : Fragment() {
    lateinit var recyclerview : RecyclerView
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val inko = Inko()


    // 먼저 위에서 변수를 선언해 놓아야 onCreateView 에서도 변수를 사용할 수 있다.
    lateinit var enrollcollege:String

    // 어답터에서 값을 fragment 로 가져오기 위한 class 구문
    inner class roomListAdapterToList {
        fun getRoomId(college: String){
            enrollcollege = college
        }
    }

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
        // 어답터에서 값을 fragment 로 가져오기 위한 class 를 adapter 로 전달
        val link = roomListAdapterToList()

        recyclerview.adapter = SelectionAdapter(collegelist,link)


        val keyword = FragmentKeyword()
        view.findViewById<AppCompatButton>(R.id.select_btn).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_content,keyword).addToBackStack(null).commit()
            databaseReference.child("college").setValue(enrollcollege)
            var englishcollege = inko.ko2en(enrollcollege)
            Firebase.messaging.subscribeToTopic(englishcollege)
            Log.d("englishcollege",englishcollege)
        }

        return view
    }
}