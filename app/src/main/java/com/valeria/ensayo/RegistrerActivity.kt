package com.valeria.ensayo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.StatsLog.logEvent
import android.view.View
import android.widget.Toast
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import com.valeria.ensayo.databinding.ActivityRegistrerBinding

class RegistrerActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegistrerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(applicationContext);
        }
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRegistrer.setOnClickListener{
            val intent = Intent(this, LogActivity::class.java)
            intent.putExtra("bandera", 0)
            startActivity(intent)
            finish()
        }

        binding.btnInicio.setOnClickListener{
            val intent = Intent(this, LogActivity::class.java)
            intent.putExtra("bandera", 1)
            startActivity(intent)
            finish()
        }

        binding.btnHuawei.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        logEvento()


        val authParams : AccountAuthParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setAuthorizationCode()
            .setEmail()
            .setIdToken()
            .setId()
            .setAccessToken()
            .createParams()

        val service : AccountAuthService = AccountAuthManager.getService(this, authParams)
        startActivityForResult(service.signInIntent, 8888)
    }

    private fun logEvento() {
        val bundle: Bundle = Bundle()
        bundle.putString("Sign-in result", System.currentTimeMillis().toString())
        val instance = HiAnalytics.getInstance(this)
        instance.onEvent("SignIn", bundle)
    }

    private fun addAuth(authAccount: AuthAccount) {
        val credential = HwIdAuthProvider.credentialWithToken(authAccount.accessToken)
        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
            // onSuccess
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            val user = it.user
            Toast.makeText(this, ""+user.uid, Toast.LENGTH_SHORT).show()
            var dbConnect: DBConnect = DBConnect(this)
            var users: Users = Users()
            users.id = user.uid.toString()
            users.user_name = user.displayName.toString()
            dbConnect.add(users)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            // onFail
            Toast.makeText(this, "Registro fallido", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Process the authorization result and obtain an ID token from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8888) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                // The sign-in is successful, and the user's ID information and ID token are obtained.
                val authAccount = authAccountTask.result
                addAuth(authAccount)
            } else {
                // The sign-in failed. No processing is required. Logs are recorded for fault locating.
                Log.e(MainActivity.TAG, "sign in failed : " + (authAccountTask.exception as ApiException).statusCode)
            }
        }
    }


}