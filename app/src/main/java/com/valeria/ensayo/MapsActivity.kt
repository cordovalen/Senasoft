package com.valeria.ensayo

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapsInitializer
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import com.huawei.hms.site.api.SearchResultListener
import com.huawei.hms.site.api.SearchServiceFactory
import com.huawei.hms.site.api.model.DetailSearchRequest
import com.huawei.hms.site.api.model.DetailSearchResponse
import com.huawei.hms.site.api.model.SearchStatus
import com.huawei.hms.site.api.model.Site
import com.valeria.ensayo.databinding.ActivityMapsBinding
import java.net.URLEncoder

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GpsClas.OnGPSEventListener, View.OnClickListener,
    HuaweiMap.OnPoiClickListener {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var gps: GpsClas
    //
    private val lastKnownLocation: LatLng = LatLng(2.43823,-76.61316)
    //Crear variable de tipo HuaweiMap
    var huMap: HuaweiMap? = null
    var mMarker: Marker? = null
    //Obtener la key del json Services
    private val KEY = "DAEDANXKQluYq5ObN3eDKyTMrRBMU8pN2dQRaArmiUYikminySPYA0W4Rf87/vD90izjVLjjaM6xqEkq1We1sEMWMUt0IIt+41e5Vg=="
    //Crear la key del Bundle
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //Iniciar el mapa
        //MapsInitializer.setApiKey(KEY)
        initHuaweiMap(savedInstanceState)
        setuGPS()
    }

    private fun initHuaweiMap(savedInstanceState: Bundle?) {
        MapsInitializer.initialize(this)

        var  mapViewBundle: Bundle? = null
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        binding.mvMapa.apply{
            onCreate(mapViewBundle)
            getMapAsync(this@MapsActivity)
        }

    }

    override fun onMapReady(p0: HuaweiMap?) {
        if(p0 != null){
            huMap = p0
            val location= LatLng(2.449497,-76.606555)
            val update= CameraUpdateFactory.newLatLngZoom(location, 18.0f)
            huMap!!.animateCamera(update)

            //Establecer region visible y relleno
            //val padding = 100
            //val latLon1 = LatLng(2.449497,-76.606555)
            //val latlon2 = LatLng(3.449497,-77.606555)
            //val latLonBound = LatLngBounds(latLon1, latlon2)
            //val cameraUpdate1 = CameraUpdateFactory.newLatLngBounds(latLonBound, padding)

            //agregar marcador
            val options1 = MarkerOptions()
                .position(LatLng(2.449497,-76.606555))
                .title("Mi casa")
                .snippet("Si vas, encuentras a mi perrita Emma")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_flag_24))
            mMarker = huMap!!.addMarker(options1)

//
//
//            //agregar circulo
//            val mCircle = huMap!!.addCircle(
//                CircleOptions()
//                .center(LatLng(2.451330,-76.607916))
//                .radius(80.0))
//            mCircle.strokeColor = R.color.teal_700



        }
    }

    private fun setuGPS() {
        gps= GpsClas(this)
        gps.gpsEventListener=this
        gps.startLocationsRequest()
    }

    override fun onStart() {

        super.onStart()
        binding.mvMapa.onStart()
    }

    override fun onStop() {

        super.onStop()
        binding.mvMapa.onStop()
    }

    override fun onPause() {

        super.onPause()
        binding.mvMapa.onPause()
    }

    override fun onDestroy() {

        super.onDestroy()
        binding.mvMapa.onDestroy()
    }

    override fun onResume() {

        super.onResume()
        binding.mvMapa.onResume()
    }

    override fun onLowMemory() {

        super.onLowMemory()
    }

    override fun onResolutionRequired(e: Exception) {
        //Esta devolución de llamada se activa si el usuario
        //no ha otorgado permisos de ubicación a la aplicación HMSCore

    }

    override fun onLastKnownLocation(lat: Double, lon: Double) {

    }

    override fun onClick(p0: View?) {
        Toast.makeText(this@MapsActivity, "ggg", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLocation(location: LatLng, zoom: Float=16.0f) {
        val update= CameraUpdateFactory.newLatLngZoom(location, zoom)
        huMap?.apply {
            clear()
            animateCamera(update)
            val marker= MarkerOptions()
                .title("You are here")
                .position(location)
            addMarker(marker)
        }

    }

    override fun onPoiClick(poi: PointOfInterest?) {
        Log.i("OnPoiClick", "Id: ${poi!!.placeId}")
        val encodedKey= URLEncoder.encode(KEY, "utf-8")
        val searchService = SearchServiceFactory.create(this@MapsActivity, encodedKey)
        val request = DetailSearchRequest().apply { siteId=poi?.placeId }
        // Create a search result listener.
        val resultListener =
            object : SearchResultListener<DetailSearchResponse?> {
                // Return search results upon a successful search.
                override fun onSearchResult(result: DetailSearchResponse?) {
                    var site: Site?=null
                    if (result == null || result.site.also { site = it } == null) {
                        Toast.makeText(this@MapsActivity, "mal", Toast.LENGTH_SHORT).show()
                        return
                    }
                    site?.let {
                        Toast.makeText(this@MapsActivity, "f", Toast.LENGTH_SHORT).show()
                        loadDialog(it) }
                }

                // Return the result code and description upon a search exception.
                override fun onSearchError(status: SearchStatus) {
                    Log.e(
                        ContentValues.TAG,
                        "Error : " + status.errorCode + " " + status.errorMessage
                    )
                }
            }
        searchService.detailSearch(request, resultListener)
    }

    private fun loadDialog(site: Site) {
        val tempMessage="${site.formatAddress}\n\n${site.poi.phone}\n\n${site.poi.websiteUrl}"
        val message=tempMessage.replace("\n\nnull","")
        AlertDialog.Builder(this).apply {
            setTitle(site.name)
            setMessage(message)
            setPositiveButton("OK"){dialog,_ ->
                dialog.dismiss()
            }
        }.create().show()

    }
}