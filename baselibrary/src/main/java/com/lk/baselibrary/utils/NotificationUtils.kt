package com.lk.baselibrary.utils

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat

import com.lk.baselibrary.common.Constant



/**
 * @author: Administrator
 * @date: 2021/6/5
 */
object NotificationUtils {

//    fun checkOpenNotification(context: Context): Int {
//        val mNotificationManager =
//            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Android 8.0及以上
//            val channel =
//                mNotificationManager.getNotificationChannel(Constant.channelId) //CHANNEL_ID是自己定义的渠道ID
//            return channel.importance
//        }
//        return -1
//    }
//
//    fun launchSetting(context: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
//            intent.putExtra(Settings.EXTRA_CHANNEL_ID, Constant.channelId)
//            context.startActivity(intent)
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.O)
//    fun createNotificationChannel(
//        context: Context,
//        channelId: String,
//        channelName: String,
//        importance: Int
//    ) {
//        val channel = NotificationChannel(channelId, channelName, importance)
//        //锁屏显示通知
//        channel.lockscreenVisibility = Notification.VISIBILITY_SECRET
//        channel.shouldShowLights()//是否会闪光
//        channel.enableLights(true)//闪光
//        //指定闪光时的灯光颜色，为了兼容低版本在上面builder上通过setLights方法设置了
//        //channel.setLightColor(Color.RED)
//        channel.canShowBadge()//桌面launcher消息角标
//        channel.setBypassDnd(true)//设置可以绕过请勿打扰模式
//        channel.enableVibration(true)//是否允许震动
//        //震动模式，第一次100ms，第二次100ms，第三次200ms，为了兼容低版本在上面builder上设置了
//        channel.vibrationPattern = longArrayOf(100, 100, 200)
//        val notificationManager =
//            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
//
//
//    fun sendNotify(context: Context, title: String, content: String) {
//        val intent = Intent(context, MainActivity::class.java)
//        val pi = PendingIntent.getActivity(context, 0, intent, 0)
//        val notification = NotificationCompat.Builder(context, Constant.channelId)
//            .setAutoCancel(true)
//            .setSmallIcon(R.drawable.icon_20_ipad)
//            .setContentTitle(title)
//            .setLargeIcon(
//                BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
//            )
//            .setContentText(content)
//            .setContentIntent(pi)
//            .setWhen(System.currentTimeMillis())
//            .setDefaults(Notification.DEFAULT_ALL)
//            .setFullScreenIntent(pi, true)
//            .build()
//        val notificationManager =
//            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(1, notification)
//    }
}