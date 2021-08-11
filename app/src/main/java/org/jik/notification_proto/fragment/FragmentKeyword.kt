package org.jik.notification_proto.fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kimcore.inko.Inko
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
import org.jik.notification_proto.R
import org.jik.notification_proto.adapter.KeywordAdapter
import org.jik.notification_proto.college.CollegeDatabase
import org.jik.notification_proto.college.CollegeEntity
import org.jik.notification_proto.keyword.KeywordDatabase
import org.jik.notification_proto.keyword.KeywordEntity
import org.jik.notification_proto.keyword.OnDeleteListener
import kotlin.concurrent.thread

@SuppressLint("StaticFieldLeak")
class FragmentKeyword : Fragment() , OnDeleteListener{
    // db
    lateinit var keyworddb : KeywordDatabase
    var keywordList = listOf<KeywordEntity>()

    // 한 영 변환
    var enroll = mutableListOf<CollegeEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_keyword, container, false)

        // college db
        var collegedb = CollegeDatabase.getInstance(activity?.applicationContext!!)!!

        // enroll 을 저장하기 위해선 db를 참조해야하기위해 백그라운드로 처리
        val r = Runnable {
             val savedContacts = collegedb.collegeDAO().getAll()
             if (savedContacts.isNotEmpty()) {
                enroll.addAll(savedContacts)
             }
            Log.d("dsfsd", enroll[0].college)



            // keyword db
        keyworddb = KeywordDatabase.getInstance(activity?.applicationContext!!)!!

        activity?.runOnUiThread {
            view.findViewById<RecyclerView>(R.id.keyword_recyclerView).layoutManager = LinearLayoutManager(activity)
        }
        getAllKeywords()

        // 키워드 등록 버튼을 누르면 실행되어야하는 함수
        view.findViewById<AppCompatButton>(R.id.enroll_btn).setOnClickListener {
            val edittext = view.findViewById<EditText>(R.id.edit_keyword).text.toString()
            view.findViewById<EditText>(R.id.edit_keyword).text.clear()
            val keyword = KeywordEntity(null, edittext)
            insertKeyword(keyword)
        }


        view.findViewById<TextView>(R.id.delete).setOnClickListener {
            view.findViewById<EditText>(R.id.edit_keyword).text.clear()
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