package org.jik.notification_proto.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jik.notification_proto.R
import org.jik.notification_proto.data.ResultDTO


class FragmentKeyword : Fragment() {
    var auth : FirebaseAuth? =null
    var firestore : FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_keyword, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        view.findViewById<TextView>(R.id.delete).setOnClickListener {
            view.findViewById<EditText>(R.id.edit_keyword).text.clear()
        }

        view.findViewById<AppCompatButton>(R.id.enroll_btn).setOnClickListener {
            var resultDTO = ResultDTO()
            resultDTO.uid = auth?.currentUser?.uid
            resultDTO.keyword = view.findViewById<EditText>(R.id.edit_keyword).text.toString()
            firestore?.collection("users")?.document(auth?.uid.toString())?.set(resultDTO)

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