package com.valeria.ensayo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.valeria.ensayo.databinding.ActivityRecursosBinding

class RecursosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecursosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecursosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnVideoV.setOnClickListener {
            val intent = Intent(this, VocalesActivity::class.java)
            startActivity(intent)
        }
        binding.btnCuento.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }


    }
}