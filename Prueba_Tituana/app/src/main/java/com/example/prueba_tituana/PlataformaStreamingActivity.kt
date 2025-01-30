package com.example.prueba_tituana


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.prueba_tituana.BaseDeDatosMemoria.Companion.arregloMPlataformaStreaming
import com.google.android.material.snackbar.Snackbar

class PlataformaStreamingActivity : AppCompatActivity() {

    private lateinit var adaptador: ArrayAdapter<MPlataformaStreaming>
    private var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plataforma_streaming)
        val listView = findViewById<ListView>(R.id.lv_list_view)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arregloMPlataformaStreaming
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_list_view)
        botonAnadirListView.setOnClickListener {
            startActivity(Intent(this, RegistrarPlatStr:class.java))
        }
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_plataforma_streaming, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {

                true
            }
            R.id.mi_eliminar -> {

                true
            }
            R.id.mi_ver_pelicula-> {

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(
            findViewById(R.id.cl_plataforma_streaming),
            texto,
            Snackbar.LENGTH_SHORT
        ).show()
    }

}
