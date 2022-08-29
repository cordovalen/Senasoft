package com.valeria.ensayo


import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class HmsMessageService: HmsMessageService() {
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        token?.let{
            Log.e("Token", it)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
    }

}