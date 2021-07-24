package org.jik.notification_proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jik.notification_proto.fragment.FragmentInitial

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val initial =FragmentInitial()
        supportFragmentManager.beginTransaction().replace(R.id.main_content,initial).commit()

    }
}

