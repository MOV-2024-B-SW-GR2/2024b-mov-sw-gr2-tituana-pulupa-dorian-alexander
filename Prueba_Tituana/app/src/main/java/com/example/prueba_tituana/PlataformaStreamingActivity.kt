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
    private var posicionItemSeleccionado = -1 // Variable global para la posición seleccionada

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
            val intentExplicito = Intent(this, crear_plataformaStreaming::class.java)
            intentExplicito.putExtra("operacion", "Añadir plataforma de Streaming")
            startActivity(intentExplicito)
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
                val intentExplicito = Intent(this, crear_plataformaStreaming::class.java)
                intentExplicito.putExtra("operacion", "Editar plataforma de Streaming")
                startActivity(intentExplicito)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogoEliminar()
                true
            }
            R.id.mi_ver_pelicula-> {
                startActivity(Intent(this, PeliculaActivity::class.java))
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

    private fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar plataforma")
        builder.setMessage("¿Está seguro de que desea eliminar esta plataforma?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            eliminarPlataforma(posicionItemSeleccionado)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    fun abrirDialogoCrearPlataforma(adaptador: ArrayAdapter<MPlataformaStreaming>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crear Plataforma Streaming")

        // Layout para los campos de entrada
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Campo de entrada para el nombre
        val inputNombre = EditText(this)
        inputNombre.hint = "Nombre"
        inputNombre.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(inputNombre)

        // Campo de entrada para suscriptores
        val inputSuscriptores = EditText(this)
        inputSuscriptores.hint = "Número de suscriptores"
        inputSuscriptores.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(inputSuscriptores)

        // Campo de entrada para el precio mensual
        val inputPrecio = EditText(this)
        inputPrecio.hint = "Precio mensual"
        inputPrecio.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        layout.addView(inputPrecio)

        // Campo de selección para disponibilidad en LATAM
        val checkboxDisponible = CheckBox(this)
        checkboxDisponible.text = "Disponible en LATAM"
        layout.addView(checkboxDisponible)

        builder.setView(layout)

        // Botón para aceptar
        builder.setPositiveButton("Crear") { dialog, which ->
            val nombre = inputNombre.text.toString()
            val suscriptores = inputSuscriptores.text.toString().toIntOrNull() ?: 0
            val precio = inputPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val disponibleEnLatam = checkboxDisponible.isChecked

            if (nombre.isNotBlank()) {
                val nuevoId = if (arregloMPlataformaStreaming.isNotEmpty()) arregloMPlataformaStreaming.maxOf { it.id } + 1 else 1
                val nuevaPlataforma = MPlataformaStreaming(
                    id = nuevoId,
                    nombre = nombre,
                    suscriptores = suscriptores,
                    precioMensual = precio,
                    disponibleEnLatam = disponibleEnLatam
                )
                arregloMPlataformaStreaming.add(nuevaPlataforma)
                adaptador.notifyDataSetChanged()
                mostrarSnackbar("Plataforma creada exitosamente")
            } else {
                mostrarSnackbar("Debe ingresar un nombre válido")
            }
        }

        // Botón para cancelar
        builder.setNegativeButton("Cancelar", null)

        // Mostrar el cuadro de diálogo
        val dialogo = builder.create()
        dialogo.show()
    }

    private fun editarNombrePlataforma(posicion: Int, nuevoNombre: String) {
        if (posicion in 0 until arregloMPlataformaStreaming.size) {
            arregloMPlataformaStreaming[posicion].nombre = nuevoNombre
            adaptador.notifyDataSetChanged()
            mostrarSnackbar("Nombre actualizado a: $nuevoNombre")
        } else {
            mostrarSnackbar("Posición inválida")
        }
    }

    private fun eliminarPlataforma(posicion: Int) {
        if (posicion in 0 until arregloMPlataformaStreaming.size) {
            arregloMPlataformaStreaming.removeAt(posicion)
            adaptador.notifyDataSetChanged()
            mostrarSnackbar("Plataforma eliminada")
        } else {
            mostrarSnackbar("Posición inválida")
        }
    }
}
