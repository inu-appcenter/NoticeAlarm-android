package org.jik.notification_proto

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FireBaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        val currentTitle = remoteMessage!!.data["title"]
//        val prefNotice = this.getSharedPreferences("lastTitle", Context.MODE_PRIVATE)
//        val lastTitle = prefNotice.getString("lastTitle", "null")

        sendNotification(remoteMessage)

//        if(currentTitle != lastTitle) {
//            Log.d("이상한오작동",remoteMessage.data["title"].toString())
//
//            if (remoteMessage.data.isNotEmpty()) {
//                sendNotification(remoteMessage)
//                Log.d("작동",remoteMessage.data["title"].toString())
//            }
//            Log.d("오작동",remoteMessage.data["title"].toString())
//            val editor = prefNotice.edit()
//            editor.putString("lastTitle", remoteMessage.data["title"]).apply()
//            editor.commit()
//        }
    }

    // 새로운 토큰이 생성 될 때 호출
//    override fun onNewToken(token: String)
//    {
//        Log.d(TAG, "Refreshed token : $token")
//        super.onNewToken(token)
//    }

    private fun sendNotification(remoteMessage: RemoteMessage)
    {
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["body"])
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Notice",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId/* ID of notification */, notificationBuilder.build())
    }

}
