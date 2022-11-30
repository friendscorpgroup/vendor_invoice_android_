package com.frenzin.invoice.teacategory.bottombar.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.frenzin.invoice.R

class NotificationMessage : AppCompatActivity() {

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_message)

        textView = findViewById(R.id.tv_message)
        val bundle = intent.extras
        textView.text = bundle!!.getString("message")
    }
}
