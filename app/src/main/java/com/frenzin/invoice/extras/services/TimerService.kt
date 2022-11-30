package com.frenzin.invoice.extras.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.frenzin.invoice.R
import com.frenzin.invoice.extras.StartServiceActivity
import java.util.*


class TimerService : Service() {

    private var notificationManager : NotificationManager? = null
    private var isStopWatchRunning : Boolean? = null
    private var stopwatchTimer : Timer ? = null
    private var updateTimer : Timer ? = null
    var timeElapsed : Int = 0

    companion object {
        const val CHANNEL_ID = "Stopwatch_Notifications"
        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"
        const val GET_STATUS = "GET_STATUS"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"
        const val STOPWATCH_ACTION = "STOPWATCH_ACTION"
        const val TIME_ELAPSED = "TIME_ELAPSED"
        const val IS_STOPWATCH_RUNNING = "IS_STOPWATCH_RUNNING"
        const val STOPWATCH_TICK = "STOPWATCH_TICK"
        const val STOPWATCH_STATUS = "STOPWATCH_STATUS"
    }

    override fun onBind(p0: Intent): IBinder? {
        Log.d("Stopwatch", "Stopwatch onBind")
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()

        val action = intent.getStringExtra(STOPWATCH_ACTION)!!

        Log.d("Stopwatch", "onStartCommand Action: $action")

        when (action) {
            START -> startStopwatch()
            PAUSE -> pauseStopwatch()
            RESET -> resetStopwatch()
            GET_STATUS -> sendStatus()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Stopwatch", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = STOPWATCH_STATUS
        statusIntent.putExtra(IS_STOPWATCH_RUNNING, isStopWatchRunning)
        statusIntent.putExtra(TIME_ELAPSED, timeElapsed)
        sendBroadcast(statusIntent)
    }

    private fun startStopwatch() {
        isStopWatchRunning = true

        sendStatus()

        stopwatchTimer = Timer()
        stopwatchTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val stopwatchIntent = Intent()
                stopwatchIntent.action = STOPWATCH_TICK

                timeElapsed++

                stopwatchIntent.putExtra(TIME_ELAPSED, timeElapsed)
                sendBroadcast(stopwatchIntent)
            }
        }, 0, 1000)
    }

    private fun pauseStopwatch() {
        stopwatchTimer!!.cancel()
        isStopWatchRunning = false
        sendStatus()
    }

    private fun resetStopwatch() {
        pauseStopwatch()
        timeElapsed = 0
        sendStatus()
    }

    private fun buildNotification(): Notification {
        val title = if (isStopWatchRunning!!) {
            "Timer!"
        } else {
            "Stopwatch is paused!"
        }

        val hours: Int = timeElapsed.div(60).div(60)
        val minutes: Int = timeElapsed.div(60)
        val seconds: Int = timeElapsed.rem(60)

        val intent = Intent(this, StartServiceActivity::class.java)
        intent.getStringExtra("stepsCount")
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val stepsCount = sharedPreferences.getInt("stepsCount", 0)

        val contentView = RemoteViews(packageName, R.layout.custom_notification)
        contentView.setImageViewResource(R.id.imgPlayPause, R.drawable.ic_play)
        contentView.setTextViewText(R.id.timerCount, "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}")
        contentView.setTextViewText(R.id.stepsCount, "$stepsCount")

        val switchIntent = Intent(PAUSE)
        switchIntent.action = PAUSE
        val pendingIntent = PendingIntent.getActivity(this, 0, switchIntent, PendingIntent.FLAG_IMMUTABLE)
        contentView.setOnClickPendingIntent(R.id.imgPlayPause, pendingIntent)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setContent(contentView)
//            .setContentText("${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}   steps : $stepsCount")
            .setColorized(true)
            .setColor(Color.parseColor("#BEAEE2"))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOnlyAlertOnce(true)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }


    private fun updateNotification() {
        notificationManager!!.notify(
            1,
            buildNotification()
        )
    }

    private fun moveToForeground() {

        if (isStopWatchRunning!!) {
            startForeground(1, buildNotification())

            updateTimer = Timer()

            updateTimer!!.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateNotification()
                }
            }, 0, 500)
        }
    }

    private fun moveToBackground() {
//        updateTimer!!.cancel()
        stopForeground(true)
    }

}