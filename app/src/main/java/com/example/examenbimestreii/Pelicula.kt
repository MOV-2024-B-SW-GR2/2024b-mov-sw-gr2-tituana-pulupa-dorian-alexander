package com.example.examenbimestreii

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
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
        val pk_pelicula = BaseDeDatos.tablas!!.obtenerPeliculasPorPlataforma(pk_peli)[posicionItemSeleccionado].id
        return when (item.itemId){
            R.id.mi_editar_pelicula ->{

                return true
            }
            R.id.mi_eliminar_pelicula->{
                BaseDeDatos.tablas!!.eliminarPelicula(pk_pelicula)
                return true
            }
            else -> super.onContextItemSelected(item)            }
    }
}