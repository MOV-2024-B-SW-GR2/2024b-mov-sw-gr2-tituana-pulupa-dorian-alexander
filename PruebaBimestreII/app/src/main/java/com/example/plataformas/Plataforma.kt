package com.example.plataformas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Plataforma : AppCompatActivity() {
    // VARIABLES GLOBALES
    private var posicionItemSeleccionado = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_plataforma)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (BaseDeDatos.tablas == null) {
            BaseDeDatos.tablas = SqliteHelperPlataformaStreaming(this)
        }
        val btnCrearPlaforma = findViewById<Button>(R.id.btn_crear_plataforma)
        btnCrearPlaforma.setOnClickListener{
            irActividad(CrearPlataforma::class.java)
        }
        val listView = findViewById<ListView>(R.id.lv_plataforma)
        val adaptador = ArrayAdapter(
            this, // Contexto
            android.R.layout.simple_list_item_1,
            BaseDeDatos.tablas!!.obtenerPlataformas()
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)
    }

    private fun irActividad(clase: Class<*>){
        startActivity(Intent(this, clase))
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // llenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_plataforma , menu)

        // obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val plataformas = BaseDeDatos.tablas!!.obtenerPlataformas()
        if (posicionItemSeleccionado < 0 || posicionItemSeleccionado >= plataformas.size) {
            Toast.makeText(this, "Error: Plataforma no encontrada", Toast.LENGTH_SHORT).show()
            return false
        }

        val plataforma = plataformas[posicionItemSeleccionado]
        val pk = plataforma.id

        return when (item.itemId) {
            R.id.mi_eliminarPlataforma -> {
                AlertDialog.Builder(this)
                    .setTitle("Confirmación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta plataforma?")
                    .setPositiveButton("Sí") { _, _ ->
                        BaseDeDatos.tablas!!.eliminarPlataformaStreaming(pk)
                        Toast.makeText(this, "Plataforma eliminada", Toast.LENGTH_SHORT).show()
                        irActividad(Plataforma::class.java)
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.mi_editarPlataforma -> {
                mostrarDialogoEditarPlataforma(plataforma)
                true
            }
            R.id.mi_ver_ubicacion -> {
                val intentExplicito = Intent(this, GgoogleMaps::class.java)
                intentExplicito.putExtra("latitud", plataforma.direccion.latitude.toDouble())
                intentExplicito.putExtra("longitud", plataforma.direccion.longitude.toDouble())
                startActivity(intentExplicito)
                true
            }
            R.id.mi_verPlataforma -> {
                val intentExplicito = Intent(this, Pelicula::class.java)
                intentExplicito.putExtra("pk", pk)
                intentExplicito.putExtra("nombrePlataforma", plataforma.nombre)
                startActivity(intentExplicito)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarDialogoEditarPlataforma(plataforma: MPlataformaStreaming) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Plataforma")

        val inputNombre = EditText(this).apply {
            hint = "Nombre"
            setText(plataforma.nombre)
        }
        val inputSuscriptores = EditText(this).apply {
            hint = "Suscriptores"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(plataforma.suscriptores.toString())
        }
        val inputPrecio = EditText(this).apply {
            hint = "Precio Mensual"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(plataforma.precioMensual.toString())
        }
        val checkDisponible = CheckBox(this).apply {
            text = "Disponible en LATAM"
            isChecked = plataforma.disponibleEnLatam
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
            addView(inputNombre)
            addView(inputSuscriptores)
            addView(inputPrecio)
            addView(checkDisponible)
        }

        builder.setView(layout)

        builder.setPositiveButton("Actualizar") { _, _ ->
            val nuevoNombre = inputNombre.text.toString().trim()
            val nuevosSuscriptores = inputSuscriptores.text.toString().toIntOrNull() ?: plataforma.suscriptores
            val nuevoPrecio = inputPrecio.text.toString().toDoubleOrNull() ?: plataforma.precioMensual
            val disponibleEnLatam = checkDisponible.isChecked

            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "Error: El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val actualizado = BaseDeDatos.tablas!!.actualizarPlataformaStreaming(
                plataforma.id, nuevoNombre, nuevosSuscriptores, nuevoPrecio, disponibleEnLatam
            )

            if (actualizado) {
                Toast.makeText(this, "Plataforma actualizada", Toast.LENGTH_SHORT).show()
                actualizarPantalla()
            } else {
                Toast.makeText(this, "Error al actualizar plataforma", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun actualizarPantalla() {
        finish()
        startActivity(intent)
    }



}