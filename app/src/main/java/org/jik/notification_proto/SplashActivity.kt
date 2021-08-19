package org.jik.notification_proto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.jik.notification_proto.college.CollegeDatabase

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var collegedb = CollegeDatabase.getInstance(applicationContext)!!

        val handler= Handler(Looper.getMainLooper()).postDelayed({
        val runnable= Runnable {
            val savedContacts = collegedb.collegeDAO().getAll()
            if (savedContacts.isNotEmpty()) {
                val intent_home = Intent(this, HomeActivity::class.java)
                startActivity(intent_home)
                finish()
//                val home = FragmentHome()
//                supportFragmentManager.beginTransaction().replace(R.id.main_content,home).commit()
            }
            if (savedContacts.isEmpty()) {
                val intent_main = Intent(this, MainActivity::class.java)
                startActivity(intent_main)
                finish()
//            }
//            val intent= Intent(this,MainActivity::class.java)
            }
        }
        val addThread = Thread(runnable)
        addThread.start()
        },2000)
    }
}