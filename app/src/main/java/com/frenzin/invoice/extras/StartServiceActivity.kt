package com.frenzin.invoice.extras

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.frenzin.invoice.R
import com.frenzin.invoice.extras.services.TimerService
import com.google.android.material.button.MaterialButton


class StartServiceActivity : AppCompatActivity() , SensorEventListener {

    lateinit var toggleButton : MaterialButton
    lateinit var toggleButtonStop : MaterialButton
    lateinit var stopwatchValueTextView : TextView
    lateinit var tv_stepsTaken : TextView
    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var timeReceiver: BroadcastReceiver
    private var isStopwatchRunning: Boolean? = null

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private val MICROSECONDS_IN_ONE_MINUTE: Int = 60000000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_service)

        toggleButton = findViewById(R.id.toggle_button)
        toggleButtonStop = findViewById(R.id.toggle_button_stop)
        stopwatchValueTextView = findViewById(R.id.stopwatch_value_text_view)
        tv_stepsTaken = findViewById(R.id.tv_stepsTaken)

        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        toggleButton.setOnClickListener {
            if (isStopwatchRunning!!)
                pauseStopwatch()
            else
                startStopwatch()
        }

        toggleButtonStop.setOnClickListener {
            if (isStopwatchRunning!!) {
                resetStopwatch()
                totalSteps = 0f
                tv_stepsTaken.text = totalSteps.toInt().toString().trim()
                val sharePrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                sharePrefs.edit().putInt("stepsCount", totalSteps.toInt()).apply()
            }
            else
                Toast.makeText(this, "Please Start Running", Toast.LENGTH_SHORT).show()

        }
    }



    override fun onResume() {
        super.onResume()

        getStopwatchStatus()

        val statusFilter = IntentFilter()
        statusFilter.addAction(TimerService.STOPWATCH_STATUS)
        statusReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(p0: Context?, p1: Intent?) {
                val isRunning = p1?.getBooleanExtra(TimerService.IS_STOPWATCH_RUNNING, false)!!
                isStopwatchRunning = isRunning
                val timeElapsed = p1.getIntExtra(TimerService.TIME_ELAPSED, 0)

                updateLayout(isStopwatchRunning!!)
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(statusReceiver, statusFilter)

        val timeFilter = IntentFilter()
        timeFilter.addAction(TimerService.STOPWATCH_TICK)
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val timeElapsed = p1?.getIntExtra(TimerService.TIME_ELAPSED, 0)!!
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(timeReceiver, timeFilter)

        running = true

        val sm = getSystemService(SENSOR_SERVICE) as SensorManager

        sm.registerListener(
            this,
            sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
            SensorManager.SENSOR_DELAY_NORMAL,
            (1 * MICROSECONDS_IN_ONE_MINUTE)
        )

    }


    private fun updateStopwatchValue(timeElapsed: Int) {
        val hours: Int = (timeElapsed / 60) / 60
        val minutes: Int = timeElapsed / 60
        val seconds: Int = timeElapsed % 60
        stopwatchValueTextView.text =
            "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"
    }

    private fun updateLayout(isStopwatchRunning: Boolean) {
        if (isStopwatchRunning) {
            toggleButton.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_pause)
            toggleButton.text = "STOP RUNNING"
//            resetImageView.visibility = View.INVISIBLE
        } else {
            toggleButton.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_play)
            toggleButton.text = "START RUNNING"
//            resetImageView.visibility = View.VISIBLE
        }
    }


    private fun getStopwatchStatus() {
        val stopwatchService = Intent(this, TimerService::class.java)
        stopwatchService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.GET_STATUS)
        startService(stopwatchService)
    }

    private fun startStopwatch() {
        val stopwatchService = Intent(this, TimerService::class.java)
        stopwatchService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.START)
        startService(stopwatchService)
    }

    private fun pauseStopwatch() {
        val stopwatchService = Intent(this, TimerService::class.java)
        stopwatchService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.PAUSE)
        startService(stopwatchService)
    }

    private fun resetStopwatch() {
        val stopwatchService = Intent(this, TimerService::class.java)
        stopwatchService.putExtra(TimerService.STOPWATCH_ACTION, TimerService.RESET)
        startService(stopwatchService)
    }

    private fun moveToForeground() {
        val stopwatchService = Intent(this, TimerService::class.java)
        stopwatchService.putExtra(
            TimerService.STOPWATCH_ACTION,
            TimerService.MOVE_TO_FOREGROUND
        )
        startService(stopwatchService)
    }

    private fun moveToBackground() {
        val stopwatchService = Intent(this, TimerService::class.java)
        stopwatchService.putExtra(
            TimerService.STOPWATCH_ACTION,
            TimerService.MOVE_TO_BACKGROUND
        )
        startService(stopwatchService)
    }

    override fun onStart() {
        super.onStart()
        moveToBackground()
    }

    override fun onPause() {
        super.onPause()

        unregisterReceiver(statusReceiver)
        unregisterReceiver(timeReceiver)
        moveToForeground()
    }



    override fun onSensorChanged(event: SensorEvent?) {


        if (running) {
            totalSteps = event!!.values[0]

            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            tv_stepsTaken.text = ("$currentSteps")

            Log.d("stepsCount", currentSteps.toString())

            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("stepsCount", currentSteps)
            editor.apply()
        }
    }

    fun resetSteps() {
        val tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        tv_stepsTaken.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tv_stepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {

        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        Log.d("MainActivity", "$savedNumber")

        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


}