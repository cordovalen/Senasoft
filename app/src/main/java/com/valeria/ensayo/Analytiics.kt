package com.valeria.ensayo

import android.app.Application
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsTools

class Analytiics: Application() {
    override fun onCreate() {
        super.onCreate()
        HiAnalyticsTools.enableLog()
        val instance = HiAnalytics.getInstance(this)
    }
}