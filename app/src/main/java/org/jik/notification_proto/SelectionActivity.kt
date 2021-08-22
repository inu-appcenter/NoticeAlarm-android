package org.jik.notification_proto

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.adapter.SelectionAdapter
import org.jik.notification_proto.api.APIS
import org.jik.notification_proto.api.UpdateModel
import org.jik.notification_proto.college.CollegeDatabase
import org.jik.notification_proto.college.CollegeEntity
import org.jik.notification_proto.data.College
import org.jik.notification_proto.fragment.FragmentKeyword
import org.jik.notification_proto.keyword.KeywordDatabase
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
class SelectionActivity : AppCompatActivity() {
    lateinit var recyclerview : RecyclerView

    // keyworddb
    lateinit var  keyworddb : KeywordDatabase

    // db
    lateinit var collegedb : CollegeDatabase
    // 먼저 위에서 변수를 선언해 놓아야 onCreateView 에서도 변수를 사용할 수 있다.
    lateinit var enrollcollege:String

    // 어답터에서 값을 fragment 로 가져오기 위한 class 구문
    inner class roomListAdapterToList {
        fun getRoomId(college: String){
            enrollcollege = college
        }
    }
    var enroll = listOf<CollegeEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)


        // db
        collegedb = CollegeDatabase.getInstance(applicationContext!!)!!
        keyworddb = KeywordDatabase.getInstance(applicationContext!!)!!

        val collegelist :MutableList<College> = mutableListOf()

        // json 파일 가져와서 collegelist에 추가시켜주기
        val assetManager: AssetManager = this?.resources?.assets!!
        val inputStream= assetManager.open("json/INU.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jObject = JSONObject(jsonString)
        val jsonArray = jObject.getJSONArray("INU")
        for (i in 0 until jsonArray.length()) {
            val iObject = jsonArray.getJSONObject(i)
            val college = iObject.getString("college")
            collegelist.add(College(college))
        }

        recyclerview = findViewById<RecyclerView>(R.id.selection_recyclerView_activity)
        recyclerview.layoutManager = LinearLayoutManager(this)
        // 어답터에서 값을 fragment 로 가져오기 위한 class 를 adapter 로 전달
        val link = roomListAdapterToList()

        // link 를 원래는 FragmentSelection 으로 되어야하는데 바꿈 일시적으로
        recyclerview.adapter = SelectionAdapter(collegelist,link)

        val keyword = FragmentKeyword()
        findViewById<AppCompatButton>(R.id.select_btn_activity).setOnClickListener {
            val what = intent.getStringExtra("what_activity")
            Log.d("what",what.toString())
            // 선택 버튼을 누르면 HomeActivity로 가게끔 해놓음
            if (what == "initial"){
                val intent_home = Intent(this, HomeActivity::class.java)
                startActivity(intent_home)
            }
            else if (what == "home"){
                finish()
            }
//            parentFragmentManager.beginTransaction().replace(R.id.main_content, keyword).addToBackStack(null).commit()
            // 전에 들어가 있던 keyword db의 내용을 삭제
            // deleteAllKeyword()
            // 전에 들어가 있던 college db의 내용을 삭제
            deleteAllCollege()
            // college db에 삽입
            val insertcollege = CollegeEntity(null, enrollcollege)
            insertCollege(insertcollege)

            // 학과 업데이트 내용을 서버로 전달
            val token = this.getSharedPreferences("token", Context.MODE_PRIVATE)?.getString("token","default value")
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
        val getTask = object : AsyncTask<Unit, Unit, Unit>(){
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
        val deleteTask = object : AsyncTask<Unit, Unit, Unit>(){
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
}