package com.valeria.ensayo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.HwAds
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import com.huawei.hms.ads.instreamad.InstreamAd
import com.huawei.hms.ads.instreamad.InstreamAdLoadListener
import com.huawei.hms.ads.instreamad.InstreamAdLoader
import com.huawei.hms.ads.instreamad.InstreamMediaStateListener
import com.huawei.hms.videokit.player.InitFactoryCallback
import com.huawei.hms.videokit.player.WisePlayer
import com.huawei.hms.videokit.player.WisePlayerFactory
import com.huawei.hms.videokit.player.WisePlayerFactoryOptions
import com.valeria.ensayo.databinding.ActivityVocalesBinding
import java.text.Format


class VocalesActivity : AppCompatActivity() {
    companion object {

        lateinit var wisePlayerFactory: WisePlayerFactory
        const val PERMISSION_LENGTH = 1
        const val CAMERA_REQ_CODE = 111

    }

    private lateinit var binding: ActivityVocalesBinding
    private var statusAds = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVocalesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        HwAds.init(this)
        publicidad()
        playVideo()
    }


    private fun publicidad() {
        statusAds = true
        binding.idContainerTwo.visibility = View.INVISIBLE

        val builder = InstreamAdLoader.Builder(this, "testy3cglm3pj0")
        val adloader = builder.setTotalDuration(30)
            .setMaxCount(1)
            .setInstreamAdLoadListener(object : InstreamAdLoadListener {
                override fun onAdFailed(p0: Int) {
                }

                override fun onAdLoaded(p0: MutableList<InstreamAd>?) {
                    binding.instreamView.setInstreamAds(p0)
                }
            }).build()

        adloader.loadAd(AdParam.Builder().build())

        binding.instreamView.setInstreamMediaStateListener(object : InstreamMediaStateListener {
            override fun onMediaPause(p0: Int) {
            }

            override fun onMediaProgress(p0: Int, p1: Int) {
            }

            override fun onMediaCompletion(p0: Int) {
                // Initialize our video player
                binding.instreamView.visibility = View.GONE
                statusAds = false

                if (!statusAds) {
                    binding.idContainerTwo.visibility = View.VISIBLE
                    val player: WisePlayer = wisePlayerFactory.createWisePlayer()
                    player.setVideoType(0);
                    player.setBookmark(10000);
                    player.cycleMode = 1;
                    //"https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"
                    //http://baobab.kaiyanapp.com/api/v1/playUrl?vid=221119&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=1111
                    // http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4
                    // set the video to play
                    player.setPlayUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")

                    // video will be played once the video is ready to load the data


                            player.setReadyListener {
                            player.start()
                        }



                    player.setView(binding.surfaceView);

                    // initiate video loading
                    player.ready()
                }
            }

            override fun onMediaError(p0: Int, p1: Int, p2: Int) {
            }

            override fun onMediaStart(p0: Int) {
            }

            override fun onMediaStop(p0: Int) {
            }

        })

    }



    private val initFactoryCallback: InitFactoryCallback = object : InitFactoryCallback {
        override fun onSuccess(wisePlayerFactory: WisePlayerFactory) {
            setWisePlayerFactory(wisePlayerFactory)
        }

        override fun onFailure(errorCode: Int, reason: String) {
        }
    }

    fun getWisePlayerFactory(): WisePlayerFactory? {
        return VocalesActivity.wisePlayerFactory
    }

    private fun setWisePlayerFactory(wisePlayerFactory: WisePlayerFactory) {
        VocalesActivity.wisePlayerFactory = wisePlayerFactory
    }

    private fun playVideo() {
        // DeviceId test is used in the demo, specific access to incoming deviceId after encryption
        val factoryOptions = WisePlayerFactoryOptions.Builder()
            .setDeviceId("F983A5C5D8AC4CF40CCF7C5379A68F5FE5A5D597DEFF26A7C33D25BBCF23A2EF").build()
        WisePlayerFactory.initFactory(this, factoryOptions, initFactoryCallback)
    }
}

