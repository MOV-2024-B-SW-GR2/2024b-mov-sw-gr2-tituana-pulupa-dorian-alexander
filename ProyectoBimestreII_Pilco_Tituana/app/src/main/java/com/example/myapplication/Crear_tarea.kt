package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Crear_tarea : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_tarea)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val descripcionTarea = findViewById<EditText>(R.id.tv_descripcion_tarea_crear)
        val materiaTarea = findViewById<EditText>(R.id.tv_materia_tarea_crear)
        val fechaTarea = findViewById<EditText>(R.id.tv_fecha_tarea_crear)
        val btnCrearTarea = findViewById<Button>(R.id.btn_crear_tarea)
        val idUsuario = intent.getIntExtra("pk_usuario", 0)
        btnCrearTarea.setOnClickListener {
            val descripcion = descripcionTarea.text.toString()
            val materia = materiaTarea.text.toString()
            val fechaEntrega = fechaTarea.text.toString()
            if (descripcion.isNotEmpty() && materia.isNotEmpty() && fechaEntrega.isNotEmpty()) {
                val sqliteHelper = SqliteHelper(this)
                val resultado = sqliteHelper.agregarTarea(descripcion, materia, fechaEntrega, idUsuario)
                Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
                descripcionTarea.text.clear()
                materiaTarea.text.clear()
                fechaTarea.text.clear()
                val intentExplicito = Intent(this, Tareas::class.java)
                intentExplicito.putExtra("pk_usuario", idUsuario)
                startActivity(intentExplicito)
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
