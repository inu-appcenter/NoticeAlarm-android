package org.jik.notification_proto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jik.notification_proto.adapter.CustomAdpater
import org.jik.notification_proto.data.Item
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        thread {
            var result = ArrayList<Item>()

            var array = arrayOf(1,2,3,4,5,6,7)
            for(i in array) {
                val doc =
                        Jsoup.connect("https://www.inu.ac.kr/user/indexSub.do?codyMenuSeq=41719&siteId=ite&dum=dum&boardId=45113&page=$i")
                                .get()
                val get_class = doc.select(".list")
                val titles = get_class.select(".title").select("a")
                val date = get_class.select("tbody>tr>td")[8]
                Log.d("date",date.toString())

                for (title in titles) {
                    val titletxt = title.text()
                    val datetxt = date.text()
                    Log.d("asd", titletxt)
                    result.add(Item(titletxt, datetxt))
                }
            }



            this.runOnUiThread {
                val recyclerViewAdapter = CustomAdpater(result)
                val linearLayoutManager = LinearLayoutManager(this)

                val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)

                recyclerview.layoutManager = linearLayoutManager
                recyclerview.adapter= recyclerViewAdapter


            }
        }
    }


    fun makeLineText(element: Elements) : String{
        val list = element.html().split("<br>").toTypedArray()
        var result = ""
        list.forEach {
            if(it == " ") return@forEach
            result+=it+"\n"
        }
        return result
    }
}

