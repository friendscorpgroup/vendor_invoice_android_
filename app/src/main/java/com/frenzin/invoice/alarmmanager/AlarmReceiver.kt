package com.frenzin.invoice.alarmmanager

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.frenzin.invoice.R
import com.frenzin.invoice.roomdatabase.DatabaseClass
import com.frenzin.invoice.roomdatabase.UserModel
import com.frenzin.invoice.teacategory.bottombar.dashboard.AddCustomerTeaActivity.Companion.TAG
import com.frenzin.invoice.ui.ChooseCategoryActivity
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    var notificationManager: NotificationManager? = null
    private var list : List<UserModel>? = null
    private var list1 : List<UserModel>? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {

        getData(context)

        if (list!!.size > 0) {

            createNotificationChannel(context)
            sendNotification(context)

            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isScreenOn = pm.isScreenOn
            if (isScreenOn == false) {
                val wl = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "myApp:MyLock"
                )
                wl.acquire(2000)
                wl.release()
            }
        }

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getBroadcast(context, 5, intent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        }
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 55*60*1000] =pendingIntent
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attr = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, "Visitor Book", importance)
            mChannel.setShowBadge(true)
            mChannel.enableVibration(true)
            mChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, attr)
            notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(mChannel)
        }
    }


    private fun sendNotification(context: Context) {

        val intent = Intent(context, ChooseCategoryActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val CHANNEL_ID = "test"
        val pendingIntent = PendingIntent.getActivity(context, 0 , intent, PendingIntent.FLAG_MUTABLE)

        val notiTitle = "Delivery Reminder"
        var notiDesc = "Delivery Reminder"

        if (list!!.size == 1){
            notiDesc = "${list!![0].fname} ${list!![0].morningtime}"
        }
        if (list!!.size == 2){
            notiDesc = "${list!![0].fname} ${list!![0].morningtime}\n"+
                    "${list!![1].fname} ${list!![1].morningtime}"
        }
        if (list!!.size == 3){
            notiDesc = "${list!![0].fname} ${list!![0].morningtime}\n"+
                    "${list!![1].fname} ${list!![1].morningtime}\n"+
                    "${list!![2].fname} ${list!![2].morningtime}"
        }
        if (list!!.size == 4){
            notiDesc = "${list!![0].fname} ${list!![0].morningtime}\n"+
                    "${list!![1].fname} ${list!![1].morningtime}\n"+
                    "${list!![2].fname} ${list!![2].morningtime}\n"+
                    "${list!![3].fname} ${list!![3].morningtime}"
        }
        else if(list!!.size > 4){
            notiDesc = "Reminder : Customer : ${list!!.size}"
        }

        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_time_watch)
            .setContentTitle(notiTitle)
            .setSound(defaultSoundUri)
            .setContentText(notiDesc)
            .setContentIntent(pendingIntent)

        val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "test"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.setShowBadge(true)
            assert(mNotifyMgr != null)
            mNotifyMgr!!.createNotificationChannel(mChannel)
        }
        assert(mNotifyMgr != null)

        val id = Random().nextInt()
        mNotifyMgr!!.notify(100 + id, mBuilder.build())
    }


    companion object {
        const val CHANNEL_ID = "AlarmReceiver"
    }


    private fun getData(context : Context) {

        val sdf = SimpleDateFormat("kk:mm", Locale.getDefault())
        val a = Date()
        val startTime = sdf.format(a)
        a.time = System.currentTimeMillis() + 60 * 60 * 1000
        val endtTime = sdf.format(a)

        Log.e(TAG, "starttime : $startTime && endtime : $endtTime")

        list = ArrayList()
        list = DatabaseClass.getDatabase(context)!!.dao!!.getCustomerMorningTimeList(startTime, endtTime)
        list1 = ArrayList()
        list1 = DatabaseClass.getDatabase(context)!!.dao!!.getCustomerEveningTimeList(startTime, endtTime)

        Log.e(TAG, "list : ${list!!.size}")
        Log.e(TAG, "list 1 : ${list1!!.size}")

    }

}
