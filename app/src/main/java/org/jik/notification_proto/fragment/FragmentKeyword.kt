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
    private val inko = Inko()
    var enroll = mutableListOf<CollegeEntity>()


    private lateinit var map: Map<String, String>
    private val databaseReference = FirebaseDatabase.getInstance().reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_keyword, container, false)

        // college db
        var collegedb = CollegeDatabase.getInstance(activity?.applicationContext!!)!!

        // enroll 을 저장하기 위해선 db를 참조해야하기위해 백그라운드로 처리
        CoroutineScope(Dispatchers.Main).launch {
            val temp = CoroutineScope(Dispatchers.Default).async {
                val savedContacts = collegedb.collegeDAO().getAll()
                enroll.addAll(savedContacts)
            }.await()

            Log.d("dsfsd", enroll[0].college)

            // firebase(remote database)에서 keyword 저장값 가지고 오기
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val result = bundle.getString("bundleKey").toString()

            FirebaseDatabase.getInstance().reference
                    .child("college").child(result)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            Log.d("datachange", result)
                            map = p0.value as Map<String, String>
                        }
                    })
        }


            // keyword db
        keyworddb = KeywordDatabase.getInstance(activity?.applicationContext!!)!!

        activity?.runOnUiThread {
            view.findViewById<RecyclerView>(R.id.keyword_recyclerView).layoutManager = LinearLayoutManager(activity)
        }
        getAllKeywords()

        // 키워드 등록 버튼을 누르면 실행되어야하는 함수
        view.findViewById<AppCompatButton>(R.id.enroll_btn).setOnClickListener {
            val edittext = view.findViewById<EditText>(R.id.edit_keyword).text.toString()
            subscribe(edittext)
            view.findViewById<EditText>(R.id.edit_keyword).text.clear()
            val keyword = KeywordEntity(null, edittext)
            insertKeyword(keyword)
        }


        view.findViewById<TextView>(R.id.delete).setOnClickListener {
            view.findViewById<EditText>(R.id.edit_keyword).text.clear()
        }

        }


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

    fun subscribe(enteredKeyword: String) {
        var englishKeyword = inko.ko2en(enteredKeyword)
        Log.d("번역된 키워드",englishKeyword)
        Firebase.messaging.subscribeToTopic(englishKeyword)  // enteredKeyword 라는 주제로 구독한다는걸 표시하는 메소드
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("OK",enteredKeyword)
                    var num = "1" // 기본값 1
                    if (map.containsKey(enteredKeyword)) {
                        num = (map.getValue(enteredKeyword).toInt() + 1).toString() // 구독자 수 +1
                    }
                    databaseReference.child("college").child(enroll[0].college).child(enteredKeyword).setValue(num)
                }
                else Log.d("network","네트워크 상태가 불안정 합니다.")
            }
    }
    // 키워드를 삭제하는 버튼을 누르면 실행되어야하는 함수
    fun unSubscribe(enteredKeyword: String){
        var englishKeyword = inko.ko2en(enteredKeyword)

        FirebaseMessaging.getInstance().unsubscribeFromTopic(englishKeyword) // enteredKeyword 라는 주제로 구독한는걸 구독 취소하는 메소드
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var num = map.getValue(enteredKeyword).toInt() - 1 // 구독자 수 -1
                    databaseReference.child("college").child(enroll[0].college).child(enteredKeyword).setValue(num.toString())
                    if(num == 0){
                        databaseReference.child("college").child(enroll[0].college).child(enteredKeyword).removeValue()
                    }
                } else Log.d("network", "네트워크 상태가 불안정 합니다.")
            }

    }

    override fun onDeleteListener(keyword: KeywordEntity) {
        deleteKeyword(keyword)

    }

    override fun onSubscribeListener(enteredKeyword: String) {
        unSubscribe(enteredKeyword)
    }
}