package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (BaseDeDatos.tablas == null) {
            BaseDeDatos.tablas = SqliteHelper(this)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed(){}
        }
        onBackPressedDispatcher.addCallback(this, callback)

        val btnIngresar = findViewById<Button>(R.id.btn_ingresar_sistema)
        val usuario = findViewById<EditText>(R.id.tv_usuario)
        val contrasena = findViewById<EditText>(R.id.tv_contrasena_usuario)

        btnIngresar.setOnClickListener {
            val cedula = usuario.text.toString()
            val password = contrasena.text.toString()

            if (cedula.isNotEmpty() && password.isNotEmpty()) {
                val resultado = BaseDeDatos.tablas?.autenticarUsuario(cedula, password)

                when (resultado) {
                    "Autenticación exitosa" -> {
                        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                        val intentExplicito = Intent(this, Tareas::class.java)
                        intentExplicito.putExtra("pk_usuario", cedula)
                        startActivity(intentExplicito)
                    }
                    "Contraseña incorrecta" -> {
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                    "No existe un usuario con esta cédula" -> {
                        Toast.makeText(this, "Usuario no encontrado. Regístrese primero.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        val btnRegistrar = findViewById<Button>(R.id.button2)
        btnRegistrar.setOnClickListener {
            val intentExplicito = Intent(this, Registro::class.java)
            startActivity(intentExplicito)
        }
    }
}
