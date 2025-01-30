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
        val pk = BaseDeDatos.tablas!!.obtenerPlataformas()[posicionItemSeleccionado].id
        return when (item.itemId){
            R.id.mi_eliminarPlataforma ->{
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
                return true
            }
            R.id.mi_editarPlataforma->{
                return true
            }
            R.id.mi_verPlataforma->{
                val nombrePlataforma =  BaseDeDatos.tablas!!.obtenerPlataformas()[posicionItemSeleccionado].nombre
                val intentExplicito = Intent(this, Pelicula::class.java)
                intentExplicito.putExtra("pk", pk)
                intentExplicito.putExtra("nombrePlataforma",nombrePlataforma)
                startActivity(intentExplicito)
                return true
            }
            else -> super.onContextItemSelected(item)            }
    }
}