package org.jik.notification_proto

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FirebaseService"

    var alarmMap :MutableMap<String,String> = mutableMapOf()

    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")

        // 토큰 값을 따로 저장해둔다.
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
        Log.i("로그: ", "성공적으로 토큰을 저장함")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)
        Log.d("알람 데이터",remoteMessage.data.toString())
        if(remoteMessage.data.isNotEmpty()){
            val keyword = remoteMessage.data["keyword"].toString()  // 키워드 값
            val name = remoteMessage.data["name"].toString()   // 공지 제목
            val link = remoteMessage.data["link"].toString()    // url


            val prefs : SharedPreferences = this.getSharedPreferences("prefs_name",Context.MODE_PRIVATE)
            // 이전의 알람 데이터들을 가져오기
            val preData = prefs.getString(keyword,"default")
            Log.d("이전 데이터",preData.toString())


            Log.i("키워드: ", keyword)
            Log.i("제목: ", name)
            Log.i("링크: ", link)


            var tempdata = preData
            tempdata += keyword + "," + name + "," +link+","
            alarmMap[keyword] = tempdata!!

            // 저장한 것 들을 SharedPreferences 로 local db에 저장
            prefs.edit().putString(keyword, alarmMap[keyword]).apply()

            sendNotification(remoteMessage)
        }

        else {
            Log.i("수신에러: ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
            Log.i("data값: ", remoteMessage.data.toString())
        }
    }


    private fun sendNotification(remoteMessage: RemoteMessage)
    {
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        // 알림 클릭하면 실행되는 activity 를 SplashActivity 로
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, uniId /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("#"+remoteMessage.data["keyword"].toString())
            .setContentText("#"+remoteMessage.data["keyword"].toString()+" 알림이 왔습니다.")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId/* ID of notification */, notificationBuilder.build())
    }

}
