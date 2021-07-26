package org.jik.notification_proto.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import org.jik.notification_proto.R


class FragmentKeyword : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_keyword, container, false)

        view.findViewById<AppCompatButton>(R.id.enroll_btn).setOnClickListener {
            view.findViewById<TextView>(R.id.textView10).text = "#" + view.findViewById<EditText>(R.id.edit_keyword).text
            view.findViewById<TextView>(R.id.textView10).visibility= View.VISIBLE
        }

        return view
    }
}