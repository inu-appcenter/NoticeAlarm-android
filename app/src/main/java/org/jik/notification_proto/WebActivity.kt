package org.jik.notification_proto

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class WebActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        var url : String?= null

        webView = findViewById(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportMultipleWindows(true)
        }
        // url 가져오기
        val title = intent.getStringExtra("title")
        val prefs : SharedPreferences = this.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)!!
        val keyword = intent.getStringExtra("keyword")
        val result = prefs.getString(keyword,"default")
        var result_lst =result?.split(",")
        Log.d("리스트",keyword.toString())
        for (i in 0 until result_lst?.size!!){
            Log.d("리스트의 각 요소",result_lst[i])
            if (result_lst[i] == title){
                url = result_lst[i+1]
                Log.d("in_url",url.toString())

            }
        }
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url.toString())


    }
    var backBtnTime = 0.toLong()

    override fun onBackPressed() {
        webView = findViewById(R.id.webView)
        val curTime = System.currentTimeMillis()
        val gapTime = curTime - backBtnTime
        when {
            webView.canGoBack() -> {
                webView.goBack()
            }
            gapTime <2000 -> {
                super.onBackPressed()
            }
            else -> {
                backBtnTime = curTime;
                Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}