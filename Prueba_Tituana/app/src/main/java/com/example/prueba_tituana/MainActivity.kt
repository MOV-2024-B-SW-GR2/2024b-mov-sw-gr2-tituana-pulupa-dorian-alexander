package com.example.prueba_tituana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_plataforma_streaming)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val botonPlataformaStreaming = findViewById<Button>(R.id.btn_plataforma_streaming)
        botonPlataformaStreaming
            .setOnClickListener{
                irActividad(PlataformaStreamingActivity::class.java)
            }
    }
    fun irActividad(clase: Class<*>)
    {
        startActivity(Intent(this, clase))
    }
}