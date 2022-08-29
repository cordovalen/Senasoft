package com.valeria.ensayo

import android.content.Context
import android.os.Looper
import android.util.Log
import com.huawei.hms.common.ApiException
import com.huawei.hms.location.*

class GpsClas (val context: Context) : LocationCallback() {
    private val TAG = "GPS Tracker"
    var isStarted:Boolean=false
        private set
    var gpsEventListener:OnGPSEventListener? =null
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun startLocationsRequest(interval: Long =1000) {
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(context)
        val mLocationRequest = LocationRequest()
        // establece el intervalo para las actualizaciones de ubicación, en milisegundos.
        mLocationRequest.interval = interval
        // establecer la prioridad de la solicitud
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        // verifique la configuración de los dispositivos antes de solicitar actualizaciones de ubicación.
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                Log.i(TAG, "check location settings success")
                //request location updates
                fusedLocationProviderClient.requestLocationUpdates(
                    mLocationRequest,
                    this,
                    Looper.getMainLooper()
                ).addOnSuccessListener{
                    Log.i(
                        TAG,
                        "requestLocationUpdatesWithCallback onSuccess"
                    )
                    isStarted=true
                }
                    .addOnFailureListener{ e ->
                        Log.e(
                            TAG,
                            "requestLocationUpdatesWithCallback onFailure:" + e.message
                        )
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "checkLocationSetting onFailure:" + e.message)
                val apiException: ApiException = e as ApiException
                when (apiException.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.e(TAG, "Resolution required")
                        gpsEventListener?.onResolutionRequired(e)
                    }
                }
            }
    }

    fun removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(this)
                .addOnSuccessListener{
                    isStarted=false
                    Log.i(
                        TAG,
                        "removeLocationUpdatesWithCallback onSuccess"
                    )
                }
                .addOnFailureListener{ e ->
                    Log.e(
                        TAG,
                        "removeLocationUpdatesWithCallback onFailure:" + e.message
                    )
                }
        } catch (e: Exception) {
            Log.e(TAG, "removeLocationUpdatesWithCallback exception:" + e.message)
        }
    }
    override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null) {
            val locations = locationResult.locations
            val lastLocation=locationResult.lastLocation
            gpsEventListener?.onLastKnownLocation(lastLocation.latitude,lastLocation.longitude)
            if (locations.isNotEmpty()) {
                for (location in locations) {
                    Log.e(
                        TAG,
                        "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.getLongitude()
                            .toString() + "," + location.getLatitude()
                            .toString() + "," + location.getAccuracy()
                    )
                }
            }
        }
    }
    interface OnGPSEventListener {
        fun onResolutionRequired(e: Exception)
        fun onLastKnownLocation(lat:Double, lon:Double)
    }
}