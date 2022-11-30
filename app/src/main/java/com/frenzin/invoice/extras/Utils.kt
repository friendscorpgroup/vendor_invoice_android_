package com.frenzin.invoice.extras

import com.frenzin.invoice.roomdatabase.UserDetailsModelTeaCoffee
import com.frenzin.invoice.roomdatabase.UserDetailsModelWater


object Utils {

    fun calculateTotalTea(mList : List<UserDetailsModelTeaCoffee>): Int {
        var totalPrice = 0
        for (element in mList) {
            totalPrice += element.teaItemMrng!!.toInt() + element.teaItemEvng!!.toInt()
        }
        return totalPrice
    }

    fun calculateTotalCoffee(mList : List<UserDetailsModelTeaCoffee>): Int {
        var totalPrice = 0
        for (element in mList) {
            totalPrice += element.coffeeItemMrng!!.toInt() + element.coffeeItemEvng!!.toInt()
        }
        return totalPrice
    }


    fun calculateTotalWaterBottle(mList : List<UserDetailsModelWater>): Int {
        var totalPrice = 0
        for (element in mList) {
            totalPrice += element.addbottle!!.toInt()
        }
        return totalPrice
    }


//    fun isMyServiceRunning(serviceClass: Class<*>, mActivity: Activity): Boolean {
//        val manager: ActivityManager =
//            mActivity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            if (serviceClass.name == service.service.getClassName()) {
//                Log.i("Service status", "Running")
//                return true
//            }
//        }
//        Log.i("Service status", "Not running")
//        return false
//    }
//
//    fun isLocationEnabledOrNot(context: Context): Boolean {
//        var locationManager: LocationManager? = null
//        locationManager =
//            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
//        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//
//    fun showAlertLocation(context: Context, title: String, message: String, btnText: String) {
//        val alertDialog = AlertDialog.Builder(context).create()
//        alertDialog.setTitle(title)
//        alertDialog.setMessage(message)
//        alertDialog.setButton(btnText) { dialog, which ->
//            dialog.dismiss()
//            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//        }
//        alertDialog.show()
//    }

}