package com.valeria.ensayo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsTools


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            if(AGConnectAuth.getInstance().currentUser == null){
                var intent = Intent(this, RegistrerActivity::class.java)
                startActivity(intent)
                finish()}
            else {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        },3000)


    }
}