package com.frenzin.invoice.ui


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.frenzin.invoice.R
import com.frenzin.invoice.alarmmanager.AlarmReceiver
import com.frenzin.invoice.teacategory.bottombar.dashboard.AddCustomerTeaActivity.Companion.TAG
import com.frenzin.invoice.teacategory.ui.HomeScreenActivityTea
import com.frenzin.invoice.watercategory.ui.HomeScreenActivityWater

class ChooseCategoryActivity : AppCompatActivity() {

    lateinit var waterSection : RelativeLayout
    lateinit var teaSection : RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_category)

//        createReminder()
        setAlarm()
        checkPrefs()
        setFindViewById()
        onClickListener()


    }

    private fun setAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val isGranted = alarmManager.canScheduleExactAlarms()
            if (isGranted) {
                SetAlarmManager(applicationContext).execute()
            } else {
                val i = Intent()
                i.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                startActivity(i)
            }
        } else {
            SetAlarmManager(applicationContext).execute()
        }
    }

    private fun setFindViewById() {
        teaSection = findViewById(R.id.layoutTea)
        waterSection = findViewById(R.id.layoutWater)
    }

    private fun onClickListener() {
        teaSection.setOnClickListener(onClickListener)
        waterSection.setOnClickListener(onClickListener)
    }

    private val onClickListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view!!.id) {
                R.id.layoutTea -> {
                    val intent = Intent(this@ChooseCategoryActivity, HomeScreenActivityTea::class.java)
                    startActivity(intent)
                }
                R.id.layoutWater -> {
                    val intent = Intent(this@ChooseCategoryActivity, HomeScreenActivityWater::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun checkPrefs() {

        val prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        if (prefs.getString("key","0").equals("TEA_COFFEE_ACTIVITY")){
            startActivity(Intent(this@ChooseCategoryActivity, HomeScreenActivityTea::class.java))
            finish()
        }
        else if (prefs.getString("key","0").equals("WATER_ACTIVITY")){
            startActivity(Intent(this@ChooseCategoryActivity, HomeScreenActivityWater::class.java))
            finish()
        }

    }


    class SetAlarmManager(val context: Context) : AsyncTask<String?, Void?, String?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg strings: String?): String? {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            val pendingIntent = PendingIntent.getBroadcast(context, 5, intent, PendingIntent.FLAG_IMMUTABLE)

            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms()
            }
            alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 55*60*1000] = pendingIntent

            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
        }
    }

}