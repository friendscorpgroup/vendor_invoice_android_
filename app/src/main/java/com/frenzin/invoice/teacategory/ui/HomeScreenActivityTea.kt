package com.frenzin.invoice.teacategory.ui

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.frenzin.invoice.R
import com.frenzin.invoice.teacategory.bottombar.dashboard.DashboardTeaFragment
import com.frenzin.invoice.teacategory.bottombar.invoice.InvoiceTeaFragment
import com.frenzin.invoice.teacategory.bottombar.profile.ProfileTeaFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class HomeScreenActivityTea : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen_tea)

        val prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("key", "TEA_COFFEE_ACTIVITY").apply()

        setBottomNavBar()
    }


    private fun setBottomNavBar() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewTea)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
        var selectedFragment: Fragment? = null
        val itemId = item.itemId
        if (itemId == R.id.dashboardT) {
            selectedFragment = DashboardTeaFragment()
        } else if (itemId == R.id.invoiceT) {
            selectedFragment = InvoiceTeaFragment()
        } else if (itemId == R.id.profileT) {
            selectedFragment = ProfileTeaFragment()
        }

        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragmentTea, selectedFragment).commit()
        }
        true
    }

    override fun onBackPressed() {
        alertDialog()
    }


    private fun alertDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to exit ?")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes"
        ) { dialog: DialogInterface?, which: Int ->
            exitApp()
        }

        builder.setNegativeButton("No"
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun exitApp() {
        finishAffinity()
    }

}