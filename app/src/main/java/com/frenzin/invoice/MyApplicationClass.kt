package com.frenzin.invoice

import android.app.Application

class MyApplicationClass : Application() {

    companion object{
        var myApp: MyApplicationClass? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
    }

}