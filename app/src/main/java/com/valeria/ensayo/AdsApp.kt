package com.valeria.ensayo

import android.app.Application
import com.huawei.hms.ads.h
import com.huawei.hms.ads.hw
import com.huawei.hms.ads.t
import com.huawei.hms.ads.HwAds

class AdsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this)
    }

}