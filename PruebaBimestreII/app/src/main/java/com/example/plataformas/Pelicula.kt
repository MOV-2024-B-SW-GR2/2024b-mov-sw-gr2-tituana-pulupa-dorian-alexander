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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Pelicula : AppCompatActivity() {
    private var posicionItemSeleccionado = -1
    private var pk_peli = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pelicula)
        val pk = intent.getIntExtra("pk", 0)
        pk_peli = pk
        val nombre = intent.getIntExtra("nombrePlataforma", 1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        if (BaseDeDatos.tablas == null) {
            BaseDeDatos.tablas = SqliteHelperPlataformaStreaming(this)
        }
        val btnCrearPlaforma = findViewById<Button>(R.id.btn_crearPeliculas)
        btnCrearPlaforma.setOnClickListener{
            val intentExplicito = Intent(this, CrearPelicula::class.java)
            intentExplicito.putExtra("pk", pk)
            startActivity(intentExplicito)
        }
        val listView = findViewById<ListView>(R.id.lv_peliculas)
        val adaptador = ArrayAdapter(
            this, // Contexto
            android.R.layout.simple_list_item_1,
            BaseDeDatos.tablas!!.obtenerPeliculasPorPlataforma(pk)
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
        inflater.inflate(R.menu.menu_pelicula , menu)

        // obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val peliculas = BaseDeDatos.tablas!!.obtenerPeliculasPorPlataforma(pk_peli)
        if (posicionItemSeleccionado < 0 || posicionItemSeleccionado >= peliculas.size) {
            Toast.makeText(this, "Error: Película no encontrada", Toast.LENGTH_SHORT).show()
            return false
        }

        val pelicula = peliculas[posicionItemSeleccionado]
        val pk = pelicula.id

        return when (item.itemId) {
            R.id.mi_eliminar_pelicula -> {
                AlertDialog.Builder(this)
                    .setTitle("Confirmación")
                    .setMessage("¿Estás seguro de que deseas eliminar esta película?")
                    .setPositiveButton("Sí") { _, _ ->
                        BaseDeDatos.tablas!!.eliminarPelicula(pk)
                        Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show()
                        actualizarPantalla()
                    }
                    .setNegativeButton("No", null)
                    .show()
                true
            }
            R.id.mi_editar_pelicula -> {
                mostrarDialogoEditarPelicula(pelicula)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarDialogoEditarPelicula(pelicula: MPelicula) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Película")

        val inputTitulo = EditText(this).apply {
            hint = "Título"
            setText(pelicula.titulo)
        }
        val inputGenero = EditText(this).apply {
            hint = "Género"
            setText(pelicula.genero)
        }
        val inputDuracion = EditText(this).apply {
            hint = "Duración"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(pelicula.duracion.toString())
        }
        val inputFechaEstreno = EditText(this).apply {
            hint = "Fecha de estreno"
            setText(pelicula.fechaEstreno)
        }
        val checkPopular = CheckBox(this).apply {
            text = "Es popular"
            isChecked = pelicula.esPopular
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
            addView(inputTitulo)
            addView(inputGenero)
            addView(inputDuracion)
            addView(inputFechaEstreno)
            addView(checkPopular)
        }

        builder.setView(layout)

        builder.setPositiveButton("Actualizar") { _, _ ->
            val nuevoTitulo = inputTitulo.text.toString().trim()
            val nuevoGenero = inputGenero.text.toString().trim()
            val nuevaDuracion = inputDuracion.text.toString().toDoubleOrNull() ?: pelicula.duracion
            val nuevaFechaEstreno = inputFechaEstreno.text.toString().trim()
            val esPopular = checkPopular.isChecked

            if (nuevoTitulo.isEmpty() || nuevoGenero.isEmpty() || nuevaFechaEstreno.isEmpty()) {
                Toast.makeText(this, "Error: Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val actualizado = BaseDeDatos.tablas!!.actualizarPelicula(
                pelicula.id, nuevoTitulo, nuevoGenero, nuevaDuracion, nuevaFechaEstreno, esPopular, pk_peli
            )

            if (actualizado) {
                Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show()
                actualizarPantalla()
            } else {
                Toast.makeText(this, "Error al actualizar película", Toast.LENGTH_SHORT).show()
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