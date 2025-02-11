package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(contexto: Context?) : SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaUsuario =
            """
                CREATE TABLE Usuario(
                    cedula VARCHAR(20) PRIMARY KEY,
                    nombre VARCHAR(50),
                    contrasena VARCHAR(50)
                )
        """.trimIndent()

        val scriptSQLCrearTablaTarea =
            """
            CREATE TABLE Tarea(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                descripcion TEXT,
                materia VARCHAR(50),
                fecha_entrega DATE,
                estado BOOLEAN,
                id_usuario INTEGER,
                FOREIGN KEY (id_usuario) REFERENCES Usuario(id) ON DELETE CASCADE
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaUsuario)
        db?.execSQL(scriptSQLCrearTablaTarea)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun autenticarUsuario(cedula: String, contrasena: String): String {
        val baseDatosLectura = readableDatabase
        val consulta = "SELECT contrasena FROM Usuario WHERE cedula = ?"
        val resultado = baseDatosLectura.rawQuery(consulta, arrayOf(cedula))
        return if (resultado.moveToFirst()) {
            val contrasenaGuardada = resultado.getString(0)
            resultado.close()
            baseDatosLectura.close()
            if (contrasenaGuardada == contrasena) {
                "Autenticación exitosa"
            } else {
                "Contraseña incorrecta"
            }
        } else {
            resultado.close()
            baseDatosLectura.close()
            "No existe un usuario con esta cédula"
        }
    }

    fun registrarUsuario(cedula: String, nombre: String, contrasena: String): String {
        val baseDatosLectura = readableDatabase
        val consulta = "SELECT * FROM Usuario WHERE cedula = ?"
        val resultado = baseDatosLectura.rawQuery(consulta, arrayOf(cedula))
        if (resultado.moveToFirst()) {
            resultado.close()
            baseDatosLectura.close()
            return "La cédula ya está registrada"
        }
        resultado.close()
        baseDatosLectura.close()
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("cedula", cedula)
            put("nombre", nombre)
            put("contrasena", contrasena)
        }
        val resultadoGuardar = baseDatosEscritura.insert("Usuario", null, valoresGuardar)
        baseDatosEscritura.close()
        return if (resultadoGuardar != -1L) {
            "Usuario registrado exitosamente"
        } else {
            "Error al registrar el usuario"
        }
    }

    fun obtenerTareasPorCedula(cedula: String): List<MTarea> {
        val baseDatosLectura = readableDatabase
        val listaTareas = mutableListOf<MTarea>()

        // Consulta para obtener las tareas de un usuario basado en la cédula (id_usuario)
        val consulta = """
        SELECT id, descripcion, materia, fecha_entrega, estado
        FROM Tarea
        WHERE id_usuario = ?
    """.trimIndent()

        // Ejecutar la consulta
        val resultado = baseDatosLectura.rawQuery(consulta, arrayOf(cedula))

        // Iterar sobre los resultados y agregar cada tarea a la lista
        while (resultado.moveToNext()) {
            listaTareas.add(
                MTarea(
                    id = resultado.getInt(0), // id
                    descripcion = resultado.getString(1), // descripcion
                    materia = resultado.getString(2), // materia
                    fecha_entrega = resultado.getString(3), // fecha_entrega
                    estado = resultado.getInt(4) == 1 // Convertir el valor entero (1/0) en un valor booleano
                )
            )
        }

        // Cerrar el cursor y la base de datos
        resultado.close()
        baseDatosLectura.close()

        // Retornar la lista de tareas
        return listaTareas
    }

    fun agregarTarea(descripcion: String, materia: String, fechaEntrega: String, idUsuario: Int): String {
        val baseDatosEscritura = writableDatabase

        // Crear los valores que vamos a insertar
        val valoresGuardar = ContentValues().apply {
            put("descripcion", descripcion)  // Descripción de la tarea
            put("materia", materia)          // Materia de la tarea
            put("fecha_entrega", fechaEntrega) // Fecha de entrega de la tarea
            put("estado", 0)                 // Estado de la tarea (0 = pendiente, 1 = cumplida)
            put("id_usuario", idUsuario)     // ID del usuario (llave foránea)
        }

        // Insertar los valores en la tabla "Tarea"
        val resultadoGuardar = baseDatosEscritura.insert("Tarea", null, valoresGuardar)
        baseDatosEscritura.close()

        // Retornar un mensaje según si la tarea fue agregada correctamente o no
        return if (resultadoGuardar != -1L) {
            "Tarea agregada exitosamente"
        } else {
            "Error al agregar la tarea"
        }
    }

    fun marcarTareaCumplida(idTarea: Int): String {
        val baseDatosEscritura = writableDatabase
        val valoresActualizar = ContentValues().apply {
            put("estado", 1)  // 1 representa que la tarea está completada
        }
        val resultadoActualizar = baseDatosEscritura.update(
            "Tarea",
            valoresActualizar,
            "id = ?",
            arrayOf(idTarea.toString())
        )
        baseDatosEscritura.close()
        return if (resultadoActualizar > 0) {
            "Tarea marcada como cumplida"
        } else {
            "Error al actualizar la tarea"
        }
    }

    fun editarTarea(idTarea: Int, descripcion: String, materia: String, fechaEntrega: String): String {
        val baseDatosEscritura = writableDatabase
        val valoresActualizar = ContentValues().apply {
            put("descripcion", descripcion)
            put("materia", materia)
            put("fecha_entrega", fechaEntrega)
        }
        val resultadoActualizar = baseDatosEscritura.update(
            "Tarea",
            valoresActualizar,
            "id = ?",
            arrayOf(idTarea.toString())
        )
        baseDatosEscritura.close()
        return if (resultadoActualizar > 0) {
            "Tarea actualizada correctamente"
        } else {
            "Error al actualizar la tarea"
        }
    }

}

