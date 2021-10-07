package org.jik.notification_proto.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kimcore.inko.Inko
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jik.notification_proto.HomeActivity
import org.jik.notification_proto.MainActivity
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.KeywordAdapter
import org.jik.notification_proto.api.APIS
import org.jik.notification_proto.api.GetModel
import org.jik.notification_proto.api.PostModel
import org.jik.notification_proto.college.CollegeDatabase
import org.jik.notification_proto.college.CollegeEntity
import org.jik.notification_proto.keyword.KeywordDatabase
import org.jik.notification_proto.keyword.KeywordEntity
import org.jik.notification_proto.keyword.OnDeleteListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

@SuppressLint("StaticFieldLeak")
class FragmentKeyword : Fragment() , OnDeleteListener{
    // db
    lateinit var keyworddb : KeywordDatabase
    var keywordList = listOf<KeywordEntity>()

    var enroll = mutableListOf<CollegeEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_keyword, container, false)

        var tmp : List<GetModel>? = null

        APIS.create().get_users("tmp","123").enqueue(object : Callback<List<GetModel>> {
            override fun onResponse(call: Call<List<GetModel>>, response: Response<List<GetModel>>) {
                tmp = response.body()
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())
                view.findViewById<TextView>(R.id.popular_1).text =tmp!![0].keyword.toString()
                view.findViewById<TextView>(R.id.popular_2).text =tmp!![1].keyword.toString()
                view.findViewById<TextView>(R.id.popular_3).text =tmp!![2].keyword.toString()
                view.findViewById<TextView>(R.id.popular_4).text =tmp!![3].keyword.toString()
                view.findViewById<TextView>(R.id.popular_5).text =tmp!![4].keyword.toString()

            }

            override fun onFailure(call: Call<List<GetModel>>, t: Throwable) {
                Log.d("log", t.printStackTrace().toString())
                Log.d("log", "fail")
            }
        })

        // college db
        var collegedb = CollegeDatabase.getInstance(activity?.applicationContext!!)!!

        // enroll 을 저장하기 위해선 db를 참조해야하기위해 백그라운드로 처리
        val r = Runnable {
             val savedContacts = collegedb.collegeDAO().getAll()
             if (savedContacts.isNotEmpty()) {
                enroll.addAll(savedContacts)
             }
            Log.d("등록된 학과", enroll[0].college)



            // keyword db
        keyworddb = KeywordDatabase.getInstance(activity?.applicationContext!!)!!

        activity?.runOnUiThread {
            // LinearLayoutManager 에서 flexbox 로 바꿈 그런데 에러가 한번 뜸 지켜봐야 할 듯
            view.findViewById<RecyclerView>(R.id.keyword_recyclerView).layoutManager = FlexboxLayoutManager(activity)
        }
        getAllKeywords()

        // 키워드 등록 버튼을 누르면 실행되어야하는 함수
        view.findViewById<AppCompatButton>(R.id.enroll_btn).setOnClickListener {
            val edittext = view.findViewById<EditText>(R.id.edit_keyword).text.toString()


            // 키워드 등록 내용을 서버로 전달
            val token =this.activity?.getSharedPreferences("token", Context.MODE_PRIVATE)?.getString("token","default value")
            Log.d("토큰 값: ",token.toString())
            val data = PostModel(major = enroll[0].college,token = token, keyword = edittext)
            Log.d("postdata",data.toString())
            APIS.create().post_users(data).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())
                    // 서버응답이 200 일 때(네트워크가 연결되었을 때, 서버가 켜져있을 때)
                    view.findViewById<EditText>(R.id.edit_keyword).text.clear()
                    val keyword = KeywordEntity(null, edittext)
                    insertKeyword(keyword)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("log", t.printStackTrace().toString())
                    Log.d("log", "fail")
                    Toast.makeText(activity!!.applicationContext,"네트워크 연결을 확인 해주세요! ", Toast.LENGTH_SHORT).show()
                }
            })
        }

        activity?.runOnUiThread {
            view.findViewById<TextView>(R.id.delete).setOnClickListener {
                view.findViewById<EditText>(R.id.edit_keyword).text.clear()
            }
        }
        }

        val addThread = Thread(r)
        addThread.start()


        return view
    }
    fun insertKeyword(keyword : KeywordEntity){
        val insertTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                keyworddb.keywordDAO().insert(keyword)
            }
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllKeywords()
            }
        }
        insertTask.execute()
    }
    fun getAllKeywords(){
        val getTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                keywordList = keyworddb.keywordDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(keywordList)
            }
        }
        getTask.execute()
    }
    fun deleteKeyword(keyword: KeywordEntity){
        val deleteTask = object :AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                keyworddb.keywordDAO().delete(keyword)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllKeywords()
            }
        }
        deleteTask.execute()
    }
    fun setRecyclerView(keywordList: List<KeywordEntity>){
        view?.findViewById<RecyclerView>(R.id.keyword_recyclerView)?.adapter = KeywordAdapter(keywordList,this)
    }

    override fun onDeleteListener(keyword: KeywordEntity) {
        deleteKeyword(keyword)

    }
}