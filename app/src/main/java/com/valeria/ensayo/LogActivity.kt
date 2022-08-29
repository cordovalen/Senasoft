package com.valeria.ensayo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.PhoneAuthProvider
import com.huawei.agconnect.auth.PhoneUser
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.valeria.ensayo.databinding.ActivityLogBinding
import java.util.*

class LogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var intent = getIntent()
        var bandera = intent.getIntExtra("bandera", 500)
        //Toast.makeText(this, ""+bandera, Toast.LENGTH_SHORT).show()
        binding.btnCode.setOnClickListener{
            obtenerCode()
        }
        if(bandera == 0){
            binding.Log.text = "LogUp"
            binding.btnGo.setOnClickListener{
            registrerCel()
            }
        } else if (bandera == 1){
            binding.Log.text = "LogIn"
            binding.btnGo.setOnClickListener {
            SigningCel()
            }
        }
    }

    private fun SigningCel() {
        var countryCode = binding.etxtCountry.text.toString()
        var phoneNumber = binding.etxtPhone.text.toString()
        var code = binding.etxtCode.text.toString()
        val credential = PhoneAuthProvider.credentialWithVerifyCode(countryCode,phoneNumber, null, code)
        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener {
            Toast.makeText(this, "Sesion iniciada", Toast.LENGTH_SHORT).show()
            // Obtain sign-in information.
            val user = AGConnectAuth.getInstance().currentUser
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            // onFail
            Toast.makeText(this, "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registrerCel() {
        val countryCode = binding.etxtCountry.text.toString()
        val phoneNumber = binding.etxtPhone.text.toString()
        val code = binding.etxtCode.text.toString()
        val phoneUser = PhoneUser.Builder()
            .setCountryCode(countryCode)
            .setPhoneNumber(phoneNumber)
            .setVerifyCode(code)
            .build()
        AGConnectAuth.getInstance().createUser(phoneUser).addOnSuccessListener {
            val user = AGConnectAuth.getInstance().currentUser
            Toast.makeText(this, ""+user.uid, Toast.LENGTH_SHORT).show()
            var dbConnect: DBConnect = DBConnect(this)
            var users: Users = Users()
            users.id = user.uid.toString()
            users.user_name = user.phone.toString()
            dbConnect.add(users)
            Toast.makeText(this, "Registro exitoso" + user.phone, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // A newly created user account is automatically signed in to your app.
        }.addOnFailureListener {
            // onFail
            Toast.makeText(this, "Registro fallido", Toast.LENGTH_SHORT).show()
        }

    }

    private fun obtenerCode() {
        val settings = VerifyCodeSettings.newBuilder()
            .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
            .sendInterval(30)
            .locale(Locale.ENGLISH)
            .build()
        val countryCode = binding.etxtCountry.text.toString()
        val phoneNumber = binding.etxtPhone.text.toString()
        val task =  AGConnectAuth.getInstance().requestVerifyCode(countryCode, phoneNumber, settings)
        task.addOnSuccessListener {
            // onSuccess
            Toast.makeText(this, "El código de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // onFail
            Toast.makeText(this, "No se pudo enviar el código", Toast.LENGTH_SHORT).show()
        }
    }
}