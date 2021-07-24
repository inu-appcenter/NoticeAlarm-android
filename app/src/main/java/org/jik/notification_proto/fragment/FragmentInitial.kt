package org.jik.notification_proto.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import org.jik.notification_proto.R


class FragmentInitial : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment__initial, container, false)

        val selection = FragmentSelection()
        view.findViewById<AppCompatButton>(R.id.initial_btn).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_content,selection).addToBackStack(null).commit()
            //(activity as MainActivity).supportFragmentManager.beginTransaction()
              //  .replace(R.id.main_content, selection).commit()
        }

        return view
    }
}