package com.example.plataformas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.maps.model.LatLng

class SqliteHelperPlataformaStreaming(
    contexto: Context? // this
): SQLiteOpenHelper(
    contexto,
    "moviles", // Nombre de la base de datos
    null,
    1 // Versión de la base de datos
) {
    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla para las plataformas de streaming
        val scriptSQLCrearTablaPlataformaStreaming = """
            CREATE TABLE PlataformaStreaming(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                suscriptores INTEGER,
                precioMensual DOUBLE,
                disponibleEnLatam BOOLEAN,
                latitud FLOAT,
                longitud FLOAT
            )
        """.trimIndent()

        // Crear la tabla de películas con la relación hacia PlataformaStreaming
        val scriptSQLCrearTablaPelicula = """
            CREATE TABLE Pelicula(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo VARCHAR(50),
                genero VARCHAR(50),
                duracion REAL,
                fechaEstreno VARCHAR(50),
                esPopular BOOLEAN,
                id_plataforma INTEGER,
                FOREIGN KEY (id_plataforma) REFERENCES PlataformaStreaming(id) ON DELETE CASCADE
            )
        """.trimIndent()

        // Ejecutar los scripts de creación de tablas
        db?.execSQL(scriptSQLCrearTablaPlataformaStreaming)
        db?.execSQL(scriptSQLCrearTablaPelicula)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // Si es necesario realizar una actualización, agregar aquí la lógica
        // p.ej., p0?.execSQL("DROP TABLE IF EXISTS PlataformaStreaming")
    }

    // Crear una nueva plataforma de streaming
    fun crearPlataformaStreaming(nombre: String, suscriptores: Int, precioMensual: Double, disponibleEnLatam: Boolean, latitud: Double, longitud: Double): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("nombre", nombre)
            put("suscriptores", suscriptores)
            put("precioMensual", precioMensual)
            put("disponibleEnLatam", disponibleEnLatam)
            put("latitud", latitud)
            put("longitud", longitud)
        }
        val resultadoGuardar = baseDatosEscritura.insert("PlataformaStreaming", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    // Obtener todas las plataformas de streaming
    fun obtenerPlataformas(): List<MPlataformaStreaming> {
        val baseDatosLectura = readableDatabase
        val listaPlataformas = mutableListOf<MPlataformaStreaming>()
        val scriptConsultaLectura = "SELECT * FROM PlataformaStreaming"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        while (resultadoConsultaLectura.moveToNext()) {
            listaPlataformas.add(
                MPlataformaStreaming(
                    resultadoConsultaLectura.getInt(0), // id
                    resultadoConsultaLectura.getString(1), // nombre
                    resultadoConsultaLectura.getInt(2), // suscriptores
                    resultadoConsultaLectura.getDouble(3), // precioMensual
                    resultadoConsultaLectura.getInt(4) == 1, // disponibleEnLatam
                    LatLng(resultadoConsultaLectura.getDouble(5), resultadoConsultaLectura.getDouble(6)) // latitud y longitud
                )
            )
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaPlataformas
    }

    // Eliminar una plataforma de streaming
    fun eliminarPlataformaStreaming(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("PlataformaStreaming", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar != -1
    }

    // Obtener las películas por plataforma
    fun obtenerPeliculasPorPlataforma(idPlataforma: Int): List<MPelicula> {
        val baseDatosLectura = readableDatabase
        val listaPeliculas = mutableListOf<MPelicula>()
        val scriptConsultaLectura = "SELECT * FROM Pelicula WHERE id_plataforma = ?"
        val parametrosConsultaLectura = arrayOf(idPlataforma.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        while (resultadoConsultaLectura.moveToNext()) {
            listaPeliculas.add(
                MPelicula(
                    resultadoConsultaLectura.getInt(0), // id
                    resultadoConsultaLectura.getString(1), // titulo
                    resultadoConsultaLectura.getString(2), // genero
                    resultadoConsultaLectura.getDouble(3), // duracion
                    resultadoConsultaLectura.getString(4), // fechaEstreno
                    resultadoConsultaLectura.getInt(5) == 1 // esPopular
                )
            )
        }

        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaPeliculas
    }

    // Crear una nueva película
    fun crearPelicula(titulo: String, genero: String, duracion: Double, fechaEstreno: String, esPopular: Boolean, idPlataforma: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("titulo", titulo)
            put("genero", genero)
            put("duracion", duracion)
            put("fechaEstreno", fechaEstreno)
            put("esPopular", esPopular)
            put("id_plataforma", idPlataforma)
        }
        val resultadoGuardar = baseDatosEscritura.insert("Pelicula", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    // Eliminar una película
    fun eliminarPelicula(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("Pelicula", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar != -1
    }

    // Actualizar plataforma de streaming
    fun actualizarPlataformaStreaming(id: Int, nombre: String, suscriptores: Int, precioMensual: Double, disponibleEnLatam: Boolean): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues().apply {
            put("nombre", nombre)
            put("suscriptores", suscriptores)
            put("precioMensual", precioMensual)
            put("disponibleEnLatam", disponibleEnLatam)
        }
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update("PlataformaStreaming", valoresAActualizar, "id=?", parametrosConsultaActualizar)
        baseDatosEscritura.close()
        return resultadoActualizar != -1
    }

    // Actualizar película
    fun actualizarPelicula(id: Int, titulo: String, genero: String, duracion: Double, fechaEstreno: String, esPopular: Boolean, idPlataforma: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues().apply {
            put("titulo", titulo)
            put("genero", genero)
            put("duracion", duracion)
            put("fechaEstreno", fechaEstreno)
            put("esPopular", esPopular)
            put("id_plataforma", idPlataforma)
        }
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update("Pelicula", valoresAActualizar, "id=?", parametrosConsultaActualizar)
        baseDatosEscritura.close()
        return resultadoActualizar != -1
    }
}
