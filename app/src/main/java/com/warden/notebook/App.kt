package com.warden.notebook

import android.app.Application
import com.tencent.mmkv.MMKV

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val rootDir = MMKV.initialize(this)
        println("mmkv root: $rootDir")
    }
}