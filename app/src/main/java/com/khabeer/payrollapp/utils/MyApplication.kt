package com.khabeer.payrollapp.utils

import android.app.Application
import com.orhanobut.hawk.Hawk

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Hawk.init(applicationContext).build()
    }
}