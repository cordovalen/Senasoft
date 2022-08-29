package com.valeria.ensayo

import android.content.Context
import android.util.Log
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task


class DBConnect {
var mCloudDB: AGConnectCloudDB
val TAG = "ConnectDB"
lateinit var mCloudDBZone: CloudDBZone
constructor(context: Context?){
    AGConnectCloudDB.initialize(context!!)
    mCloudDB = AGConnectCloudDB.getInstance()
    try {
        mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
    } catch (e: AGConnectCloudDBException) {
        e.printStackTrace()
    }
    val mConfig = CloudDBZoneConfig(
        "EnsayoZona",
        CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
        CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC
    )
    mConfig.persistenceEnabled = true
    try {
        mCloudDBZone = mCloudDB.openCloudDBZone(mConfig, true)
    } catch (e: AGConnectCloudDBException) {
        Log.e("TAG", "openCloudDBZone: " + e.message)
    }
}

    fun add(objectDb: CloudDBZoneObject?) {
        val upsertTask = mCloudDBZone.executeUpsert(objectDb!!)
        upsertTask.addOnSuccessListener(OnSuccessListener<Int> { cloudDBZoneResult ->
            Log.e(
                "TAG",
                "Upsert $cloudDBZoneResult records"
            )
        }).addOnFailureListener(
            OnFailureListener { Log.e("TAG", "Insert failed") })
    }

    fun delete(objectDb: CloudDBZoneObject?){
        val deleteTask = mCloudDBZone.executeDelete(objectDb!!)
        deleteTask.addOnSuccessListener (OnSuccessListener<Int> { cloudDBZoneResult ->
            Log.e(
                "TAG",
                "Delete $cloudDBZoneResult"
            )
        }).addOnFailureListener(
            OnFailureListener { Log.e("TAG", "Delete failed") })
    }
}