package com.example.plataformas


import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CrearPlataforma : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_plataforma)
        if (BaseDeDatos.tablas == null) {
            BaseDeDatos.tablas = SqliteHelperPlataformaStreaming(this)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnCrearPlataforma = findViewById<Button>(R.id.btn_registarPlataforma)
        btnCrearPlataforma.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.input_nombrePlataforma).text.toString().trim()
            val suscriptoresText = findViewById<EditText>(R.id.input_suscriptoresPlataforma).text.toString().trim()
            val precioText = findViewById<EditText>(R.id.input_precioPlataforma).text.toString().trim()
            val dispLatam = findViewById<Switch>(R.id.switch_disponibilidadLatam).isChecked
            val latlong = findViewById<EditText>(R.id.input_direccion)
            val latlongTexto = latlong.text.toString().trim()
            val partes = latlongTexto.split(",")
            val latitud = partes.getOrNull(0)?.toDoubleOrNull() // Intentar obtener latitud
            val longitud = partes.getOrNull(1)?.toDoubleOrNull() // Intentar obtener longitud

            if (nombre.isEmpty() || suscriptoresText.isEmpty() || precioText.isEmpty()) {
                mostrarSnackbar("Todos los campos son obligatorios")
                return@setOnClickListener
            }

            val suscriptores = suscriptoresText.toIntOrNull()
            val precio = precioText.toDoubleOrNull()
            if (suscriptores == null || precio == null) {
                mostrarSnackbar("Los valores numéricos no son válidos")
                return@setOnClickListener
            }

            if (latitud == null || longitud == null) {
                mostrarSnackbar("Las coordenadas no son válidas")
                return@setOnClickListener
            }

            val respuesta = BaseDeDatos.tablas?.crearPlataformaStreaming(
                nombre, suscriptores, precio, dispLatam, latitud, longitud
            )

// Mostrar mensaje de éxito o error
            if (respuesta == true) {
                mostrarSnackbar("Plataforma creada")
                irActividad(Plataforma::class.java)
            } else {
                mostrarSnackbar("Error al crear la plataforma")
            }

        }
    }
    private fun mostrarSnackbar(texto: String){
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    private fun irActividad(clase: Class<*>){
        startActivity(Intent(this, clase))
    }
}