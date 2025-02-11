package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseDeDatos.tablas == null) {
            BaseDeDatos.tablas = SqliteHelper(this)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nombreUsuario = findViewById<EditText>(R.id.tv_nombre_usario_r)
        val cedulaUsuario = findViewById<EditText>(R.id.tv_cedula_usario_r)
        val contrasenaUsuario = findViewById<EditText>(R.id.tv_contrasena_usario_r)
        val btnRegistrar = findViewById<Button>(R.id.btn_confirmar_registro)

        val baseDeDatos = SqliteHelper(this)

        btnRegistrar.setOnClickListener {
            val nombre = nombreUsuario.text.toString().trim()
            val cedula = cedulaUsuario.text.toString().trim()
            val contrasena = contrasenaUsuario.text.toString().trim()

            if (nombre.isNotEmpty() && cedula.isNotEmpty() && contrasena.isNotEmpty()) {
                // Verificar si la cédula ya está registrada
                val resultadoExistente = baseDeDatos.registrarUsuario(cedula, nombre, contrasena)
                if (resultadoExistente == "La cédula ya está registrada") {
                    Toast.makeText(this, resultadoExistente, Toast.LENGTH_SHORT).show()
                } else {
                    val resultado = baseDeDatos.registrarUsuario(cedula, nombre, contrasena)
                    Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()

                    if (resultado == "Usuario registrado exitosamente") {
                        finish() // Cierra la actividad de registro
                    }
                }
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
