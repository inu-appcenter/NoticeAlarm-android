package org.jik.notification_proto.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import org.jik.notification_proto.R
import org.jik.notification_proto.SelectionActivity
import org.jik.notification_proto.college.CollegeDatabase
import org.jik.notification_proto.college.CollegeEntity
import org.jik.notification_proto.keyword.KeywordEntity

@SuppressLint("StaticFieldLeak")

class FragmentInitial : Fragment() {

    var collegedb : CollegeDatabase? = null
    var enroll = mutableListOf<CollegeEntity>()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment__initial, container, false)

        val selection = FragmentSelection()
        view.findViewById<AppCompatButton>(R.id.initial_btn).setOnClickListener {
            // 그냥 selection 액티비티로 화면 전환
            val intent_selection = Intent(activity, SelectionActivity::class.java)
            // 후 처리를 위해 어느 곳에서 왔는지 값을 전달
            intent_selection.putExtra("what_activity","initial")
            startActivity(intent_selection)
//            parentFragmentManager.beginTransaction().replace(R.id.main_content,selection).addToBackStack(null).commit()
            //(activity as MainActivity).supportFragmentManager.beginTransaction()
              //  .replace(R.id.main_content, selection).commit()
        }

        var collegedb = CollegeDatabase.getInstance(activity?.applicationContext!!)!!

        val r = Runnable {
            val savedContacts = collegedb.collegeDAO().getAll()
            if (savedContacts.isNotEmpty()) {
                enroll.addAll(savedContacts)
                view.findViewById<TextView>(R.id.enroll_college).text = enroll[0].college
            }
            if (savedContacts.isEmpty()){
                activity?.runOnUiThread {
                    Toast.makeText(context, "학과를 선택해주세요!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        val addThread = Thread(r)
        addThread.start()



        return view
    }

}