package com.example.examenbimestreii

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CrearPelicula : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (BaseDeDatos.tablas == null) {
            BaseDeDatos.tablas = SqliteHelperPlataformaStreaming(this)
        }
        setContentView(R.layout.activity_crear_pelicula)
        val pk = intent.getIntExtra("pk", 0)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val agregarPelicula = findViewById<Button>(R.id.btn_agre_pel)
        val titulo = findViewById<EditText>(R.id.input_tituloPelicula)
        val genero = findViewById<EditText>(R.id.input_generoPelicula)
        val duracion = findViewById<EditText>(R.id.input_duracionPelicula)
        val fechaEstreno = findViewById<EditText>(R.id.input_fechaEstPelicula)
        val esPopular = findViewById<Switch>(R.id.switch_popularidad).isChecked
        agregarPelicula.setOnClickListener {
            val tituloTexto = titulo.text.toString().trim()
            val generoTexto = genero.text.toString().trim()
            val duracionTexto = duracion.text.toString().trim()
            val fechaEstrenoTexto = fechaEstreno.text.toString().trim()
            if (tituloTexto.isEmpty() || generoTexto.isEmpty() || duracionTexto.isEmpty() || fechaEstrenoTexto.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val duracionDouble = duracionTexto.toDoubleOrNull()
            if (duracionDouble == null || duracionDouble <= 0) {
                Toast.makeText(this, "Ingresa una duración válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            BaseDeDatos.tablas!!.crearPelicula(tituloTexto, generoTexto, duracionDouble, fechaEstrenoTexto, esPopular, pk)
            Toast.makeText(this, "Película agregada exitosamente", Toast.LENGTH_SHORT).show()
            val intentExplicito = Intent(this, Pelicula::class.java)
            intentExplicito.putExtra("pk", pk)
            startActivity(intentExplicito)
        }
    }
}