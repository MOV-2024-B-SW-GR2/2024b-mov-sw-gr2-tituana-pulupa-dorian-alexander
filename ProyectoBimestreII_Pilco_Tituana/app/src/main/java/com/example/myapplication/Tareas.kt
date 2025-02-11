package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment

class Tareas : AppCompatActivity() {
    private var posicionItemSeleccionado = -1
    private var pk_ced_doctor = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tareas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pk_doc = intent.getIntExtra("pk_usuario", 0)
        pk_ced_doctor = pk_doc;
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed(){}
        }
        onBackPressedDispatcher.addCallback(this, callback)
        val btn_agregar = findViewById<Button>(R.id.button4)
        val btn_salir = findViewById<Button>(R.id.button5)
        btn_agregar.setOnClickListener {
            val intentExplicito = Intent(this, Crear_tarea::class.java)
            intentExplicito.putExtra("pk_usuario", pk_doc)
            startActivity(intentExplicito)
        }
        btn_salir.setOnClickListener {
            val intentExplicito = Intent(this, MainActivity::class.java)
            startActivity(intentExplicito)
        }

        val listView = findViewById<ListView>(R.id.lv_tareas)
        val adaptador = ArrayAdapter(
            this, // Contexto
            android.R.layout.simple_list_item_1,
            BaseDeDatos.tablas!!.obtenerTareasPorCedula(pk_doc.toString())
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_tareas , menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    class EditarTareaDialogFragment(private val tareaId: Int) : DialogFragment() {
        private lateinit var descripcionEditText: EditText
        private lateinit var materiaEditText: EditText
        private lateinit var fechaEditText: EditText

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(android.R.layout.simple_list_item_1, container, false)
            descripcionEditText = EditText(context).apply {
                hint = "Descripción"
            }
            materiaEditText = EditText(context).apply {
                hint = "Materia"
            }
            fechaEditText = EditText(context).apply {
                hint = "Fecha de entrega"
            }
            val descripcionLabel = TextView(context).apply {
                text = "Descripción:"
                textSize = 16f
            }
            val materiaLabel = TextView(context).apply {
                text = "Materia:"
                textSize = 16f
            }
            val fechaLabel = TextView(context).apply {
                text = "Fecha de entrega:"
                textSize = 16f
            }
            val btnGuardar = Button(context).apply {
                text = "Guardar"
                setOnClickListener {
                    val descripcion = descripcionEditText.text.toString()
                    val materia = materiaEditText.text.toString()
                    val fechaEntrega = fechaEditText.text.toString()
                    val resultado = SqliteHelper(requireContext()).editarTarea(tareaId, descripcion, materia, fechaEntrega)
                    Toast.makeText(requireContext(), resultado, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(descripcionLabel)   // Agregar el título "Descripción:"
                addView(descripcionEditText) // Agregar el EditText para la descripción

                addView(materiaLabel) // Agregar el título "Materia:"
                addView(materiaEditText) // Agregar el EditText para la materia

                addView(fechaLabel) // Agregar el título "Fecha de entrega:"
                addView(fechaEditText) // Agregar el EditText para la fecha

                addView(btnGuardar) // Agregar el botón "Guardar"
                setPadding(20, 20, 20, 20) // Agregar padding al layout
            }

            dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            return layout
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_tarea -> {
                val tareaSeleccionada = BaseDeDatos.tablas!!.obtenerTareasPorCedula(pk_ced_doctor.toString())[posicionItemSeleccionado]
                val idTarea = tareaSeleccionada.id
                val dialogFragment = EditarTareaDialogFragment(idTarea)
                dialogFragment.show(supportFragmentManager, "editarTareaDialog")
                true
            }
            R.id.mi_tarea_cumplida -> {
                val tareaSeleccionada = BaseDeDatos.tablas!!.obtenerTareasPorCedula(pk_ced_doctor.toString())[posicionItemSeleccionado]
                val idTarea = tareaSeleccionada.id
                val resultado = SqliteHelper(this).marcarTareaCumplida(idTarea)
                Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
                val listView = findViewById<ListView>(R.id.lv_tareas)
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    BaseDeDatos.tablas!!.obtenerTareasPorCedula(pk_ced_doctor.toString())
                )
                listView.adapter = adaptador
                adaptador.notifyDataSetChanged()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}