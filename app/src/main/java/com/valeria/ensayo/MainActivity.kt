package com.valeria.ensayo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.InterstitialAd
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import com.valeria.ensayo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "Account kid"
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var toolbar = findViewById<Toolbar>(R.id.tollbar)
        setSupportActionBar(toolbar)
        publicidaBanner()
        binding.btnVocales.setOnClickListener {
            val intent = Intent(this, RecursosActivity::class.java)
            startActivity(intent)
            HwAds.init(this)
            requestPermissions()
        }
       // binding.txt.animate().tranlation.setduration(0).withendaaction{}
        }



    private fun publicidaBanner() {
        var bannerView = binding.hwBanner
        bannerView = BannerView(this)
        bannerView.adId = "testw6vs28auh3"
        val adSize: BannerAdSize = BannerAdSize.BANNER_SIZE_360_57
        bannerView.bannerAdSize = adSize
        binding.flBanner.addView(bannerView)
        bannerView.loadAd(AdParam.Builder().build())


    }

    private fun requestPermissions(){
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            VocalesActivity.CAMERA_REQ_CODE
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size < VocalesActivity.PERMISSION_LENGTH || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED){
            requestPermissions()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout ->
                cerrarSesion()
        }
        return true
    }

    private fun cerrarSesion() {
        AGConnectAuth.getInstance().signOut()
        Toast.makeText(this, "La cuenta se cerro correctamente", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, RegistrerActivity::class.java)
        startActivity(intent)
    }


}
