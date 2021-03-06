package org.jik.notification_proto.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.os.AsyncTask
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
import org.jik.notification_proto.adapter.SelectionAdapter
import org.jik.notification_proto.api.APIS
import org.jik.notification_proto.api.UpdateModel
import org.jik.notification_proto.college.CollegeDatabase
import org.jik.notification_proto.college.CollegeEntity
import org.jik.notification_proto.data.College
import org.jik.notification_proto.keyword.KeywordDatabase
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class FragmentSelection : Fragment() {
    lateinit var recyclerview : RecyclerView

    // keyworddb
    lateinit var  keyworddb : KeywordDatabase

    // db
    lateinit var collegedb : CollegeDatabase
    var enroll = listOf<CollegeEntity>()

    // 전의 학과를 알기위해 생성
//    var lastcollege = mutableListOf<CollegeEntity>()


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

        // db
        collegedb = CollegeDatabase.getInstance(activity?.applicationContext!!)!!
        keyworddb = KeywordDatabase.getInstance(activity?.applicationContext!!)!!

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

        // link 를 원래는 FragmentSelection 으로 되어야하는데 바꿈 일시적으로

//        recyclerview.adapter = SelectionAdapter(collegelist,link)

//        val r = Runnable {
//            val savedContacts = collegedb.collegeDAO().getAll()
//            if (lastcollege.isNotEmpty()) {
//                lastcollege.removeAt(0)
//            }
//
//            if (savedContacts.isNotEmpty()) {
//                lastcollege.addAll(savedContacts)
//            }
//        }
//        val addThread = Thread(r)
//        addThread.start()


        val keyword = FragmentKeyword()
        view.findViewById<AppCompatButton>(R.id.select_btn).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_content, keyword).commit()
            // 전에 들어가 있던 keyword db의 내용을 삭제
            // deleteAllKeyword()
            // 전에 들어가 있던 college db의 내용을 삭제
            deleteAllCollege()
            // college db에 삽입
            val insertcollege = CollegeEntity(null, enrollcollege)
            insertCollege(insertcollege)

            // 학과 업데이트 내용을 서버로 전달
            val token = this.activity?.getSharedPreferences("token", Context.MODE_PRIVATE)?.getString("token","default value")
            var updatedata = UpdateModel(token = token, major = enrollcollege)
            Log.d("updatedata", updatedata.toString())
            APIS.create().update_users(updatedata).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("log", t.printStackTrace().toString())
                    Log.d("log", "fail")
                }
            })
        }
        return view
    }

    fun insertCollege(college: CollegeEntity){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                collegedb.collegeDAO().insert(college)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllColleges()
            }
        }
        insertTask.execute()
    }

    fun getAllColleges(){
        val getTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                enroll = collegedb.collegeDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
            }
        }
        getTask.execute()
    }

    // db의 모든 데이터를 지우는 함수
    fun deleteAllCollege(){
        val deleteTask = object :AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                collegedb.collegeDAO().deleteAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllColleges()
            }
        }
        deleteTask.execute()
    }

    // keyword 를 다 지우는 함수
//    fun deleteAllKeyword(){
//        val deletekeywordTask = object :AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg p0: Unit?) {
//                keyworddb.keywordDAO().deleteAll()
//            }
//        }
//        deletekeywordTask.execute()
//    }
}