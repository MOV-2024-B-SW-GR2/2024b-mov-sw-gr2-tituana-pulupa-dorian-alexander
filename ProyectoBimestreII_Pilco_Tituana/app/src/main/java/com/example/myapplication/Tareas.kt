package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tareas : AppCompatActivity() {
    private var posicionItemSeleccionado = -1
    private var pk_ced_doctor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        // Ajuste de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtención del pk_ced_doctor desde el intent
        val pk_doc = intent.getIntExtra("pk_usuario", 0)
        pk_ced_doctor = pk_doc

        // Botón para agregar nueva tarea
        val btn_agregar = findViewById<Button>(R.id.button4)
        val btn_salir = findViewById<Button>(R.id.button5)

        // Acción de agregar tarea
        btn_agregar.setOnClickListener {
            val intentExplicito = Intent(this, Crear_tarea::class.java)
            intentExplicito.putExtra("pk_usuario", pk_doc)
            startActivity(intentExplicito)
        }

        // Acción de salir
        btn_salir.setOnClickListener {
            val intentExplicito = Intent(this, MainActivity::class.java)
            startActivity(intentExplicito)
        }

        // ListView de tareas
        val listView = findViewById<ListView>(R.id.lv_tareas)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            SqliteHelper(this).obtenerTareasPorCedula(pk_doc.toString())
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        // Registro para el menú contextual
        registerForContextMenu(listView)
    }

    // Crear el menú contextual
    override fun onCreateContextMenu(
        menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_tareas, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    // Manejo de las opciones del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_tarea -> {
                val tareaSeleccionada = SqliteHelper(this).obtenerTareasPorCedula(pk_ced_doctor.toString())[posicionItemSeleccionado]
                val idTarea = tareaSeleccionada.id

                // Llamamos al método para editar la tarea
                mostrarDialogoEditarTarea(idTarea)
                true
            }
            R.id.mi_tarea_cumplida -> {
                val tareaSeleccionada = SqliteHelper(this).obtenerTareasPorCedula(pk_ced_doctor.toString())[posicionItemSeleccionado]
                val idTarea = tareaSeleccionada.id
                val resultado = SqliteHelper(this).marcarTareaCumplida(idTarea)
                Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
                actualizarLista()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Método para mostrar el diálogo de edición de tarea
    private fun mostrarDialogoEditarTarea(tareaId: Int) {
        val tareaSeleccionada = SqliteHelper(this).obtenerTareasPorCedula(pk_ced_doctor.toString()).first { it.id == tareaId }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Tarea")

        // EditText para los campos
        val inputDescripcion = EditText(this).apply { setText(tareaSeleccionada.descripcion) }
        val inputMateria = EditText(this).apply { setText(tareaSeleccionada.materia) }
        val inputFecha = EditText(this).apply { setText(tareaSeleccionada.fecha_entrega) }

        // Layout para organizar los campos
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
            addView(inputDescripcion)
            addView(inputMateria)
            addView(inputFecha)
        }

        builder.setView(layout)

        // Acción para el botón "Actualizar"
        builder.setPositiveButton("Actualizar") { _, _ ->
            val nuevaDescripcion = inputDescripcion.text.toString().trim()
            val nuevaMateria = inputMateria.text.toString().trim()
            val nuevaFecha = inputFecha.text.toString().trim()

            // Validación para asegurarse de que no haya campos vacíos
            if (nuevaDescripcion.isEmpty() || nuevaMateria.isEmpty() || nuevaFecha.isEmpty()) {
                Toast.makeText(this, "Error: No se permiten campos vacíos", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            // Actualización de la tarea en la base de datos
            val resultado = SqliteHelper(this).editarTarea(tareaId, nuevaDescripcion, nuevaMateria, nuevaFecha)
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
            actualizarLista()
        }

        // Acción para el botón "Cancelar"
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    // Método para actualizar la lista de tareas después de una modificación
    private fun actualizarLista() {
        val listView = findViewById<ListView>(R.id.lv_tareas)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            SqliteHelper(this).obtenerTareasPorCedula(pk_ced_doctor.toString())
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
    }
}
