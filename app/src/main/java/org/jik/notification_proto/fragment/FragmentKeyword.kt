package org.jik.notification_proto.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import org.jik.notification_proto.R


class FragmentKeyword : Fragment() {
    private lateinit var map: Map<String, String>
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_keyword, container, false)

        FirebaseDatabase.getInstance().reference
                .child("keywords")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        map = p0.value as Map<String, String>
                    }
                })

        fun subscribe(enteredKeyword: String) {
                Firebase.messaging.subscribeToTopic(enteredKeyword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var num = "1" // 기본값 1
                            if (map.containsKey(enteredKeyword)) {
                                num = (map.getValue(enteredKeyword).toInt() + 1).toString() // 구독자 수 +1
                            }
                            databaseReference.child("keywords").child(enteredKeyword).setValue(num)
                            Log.d("asd",enteredKeyword)
                        }

                        else {
                            Log.d("network","네트워크 상태가 불안정 합니다.")
                        }
                    }
        }
        view.findViewById<TextView>(R.id.delete).setOnClickListener {
            view.findViewById<EditText>(R.id.edit_keyword).text.clear()
        }

        view.findViewById<AppCompatButton>(R.id.enroll_btn).setOnClickListener {
            subscribe(view.findViewById<EditText>(R.id.edit_keyword).text.toString())


            if (view.findViewById<TextView>(R.id.textView1)?.visibility == View.INVISIBLE){
                view.findViewById<TextView>(R.id.textView1).text = "#" + view.findViewById<EditText>(R.id.edit_keyword).text
                view.findViewById<TextView>(R.id.textView1).visibility = View.VISIBLE
            }
            else if (view.findViewById<TextView>(R.id.textView1)?.visibility == View.VISIBLE && view.findViewById<TextView>(R.id.textView2)?.visibility == View.INVISIBLE){
                view.findViewById<TextView>(R.id.textView2).text = "#" + view.findViewById<EditText>(R.id.edit_keyword).text
                view.findViewById<TextView>(R.id.textView2).visibility = View.VISIBLE

            }
            else if (view.findViewById<TextView>(R.id.textView2)?.visibility == View.VISIBLE && view.findViewById<TextView>(R.id.textView3)?.visibility == View.INVISIBLE){
                view.findViewById<TextView>(R.id.textView3).text = "#" + view.findViewById<EditText>(R.id.edit_keyword).text
                view.findViewById<TextView>(R.id.textView3).visibility = View.VISIBLE
            }
            else if (view.findViewById<TextView>(R.id.textView3)?.visibility == View.VISIBLE){
                view.findViewById<TextView>(R.id.textView4).text = "#" + view.findViewById<EditText>(R.id.edit_keyword).text
                view.findViewById<TextView>(R.id.textView4).visibility = View.VISIBLE
            }
        }


        return view
    }
}