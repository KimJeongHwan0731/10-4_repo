package com.example.exam10practice

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.exam10practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var manager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNotificate.setOnClickListener {
            // 1. notificationManager 객체참조변수
            // 1. notificationCompat.Builder 객체참조변수
            manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder

            // 2. channel 객체참조변수를 만든다. (API 26버전 이상부터 채널을 만들어줘야됨)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 3. 26버전 이상이므로 채널객체참조변수를 만들어야됨
                val channelID = "kjh-channel"
                val channelName = "My KJH Channel"
                val channel =
                    NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)

                //채널에 대한 정보등록
                channel.description = "My KJH Channel Description"
                channel.setShowBadge(true)
                //알림음 오디오 설정
                val notificationUri: Uri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributesBuild = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                channel.setSound(notificationUri, audioAttributesBuild)
                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

                // 4.채널을 notificationManager에 등록한다.
                manager.createNotificationChannel(channel)

                // 5. 채널아이디를 이용해서 빌더생성
                builder = NotificationCompat.Builder(this, channelID)
            } else {
                // 5. 채널아이디를 이용하지 않고 빌더생성
                builder = NotificationCompat.Builder(this)
            }

            // 6. builder 알림창이 어떤 방법으로 구현될지 보여주는것
            builder.setSmallIcon(R.drawable.ic_notification_overlay)
            builder.setWhen(System.currentTimeMillis())
            builder.setContentTitle("My First Notification")
            builder.setContentText("My First Notification content")
            builder.setAutoCancel(true) // 수신된 notificate 메세지 삭제 가능/불가능 여부 결정
            //builder.setOngoing(true) // 수신된 notificate 메세지 스와이핑 동작 가능/불가능 여부 결정

            // 7. 수신된 알림 선택 시, 지정된 액티비티로 화면이 전환되는 pendingIntent 기능 부여
            // 7. 수신된 알림 선택 시, broadCast 화면으로 정보를 알려줌
            //var intent = Intent(this, DetailActivity::class.java)
            //val pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_IMMUTABLE)
            //builder.setContentIntent(pendingIntent)

            // 8. 알림에 액션등록하기
            val actionIntent = Intent(this, OneReceiver::class.java)
            val actionPendingIntent =
                PendingIntent.getBroadcast(this, 20, actionIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.stat_notify_more,
                    "Action",
                    actionPendingIntent
                ).build()
            )

            // 9. manager를 통해 알림 발생
            manager.notify(11, builder.build())
        }
        binding.btnNotificateCancel.setOnClickListener {
            manager.cancel(11)
        }
    }
}