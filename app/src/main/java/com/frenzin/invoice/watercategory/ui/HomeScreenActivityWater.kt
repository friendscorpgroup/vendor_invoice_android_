package com.frenzin.invoice.watercategory.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.frenzin.invoice.R
import com.frenzin.invoice.watercategory.bottombar.dashboard.DashboardWaterFragment
import com.frenzin.invoice.watercategory.bottombar.invoice.InvoiceWaterFragment
import com.frenzin.invoice.watercategory.bottombar.profile.ProfileWaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreenActivityWater : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("key", "WATER_ACTIVITY").apply()

        setBottomNavBar()
    }

    private fun setBottomNavBar() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            var selectedFragment: Fragment? = null
            val itemId = item.itemId
            if (itemId == R.id.dashboardWater) {
                selectedFragment = DashboardWaterFragment()
            } else if (itemId == R.id.invoiceWater) {
                selectedFragment = InvoiceWaterFragment()
            } else if (itemId == R.id.profileWater) {
                selectedFragment = ProfileWaterFragment()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, selectedFragment).commit()
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