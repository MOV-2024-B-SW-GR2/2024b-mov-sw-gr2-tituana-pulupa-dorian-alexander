package com.example.examenbimestreii

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelperPlataformaStreaming(
    contexto: Context? // this
): SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
)  {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaPlataformaStreaming =
            """
                CREATE TABLE PlataformaStreaming(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50),
                    suscriptores INTEGER,
                    precioMensual INTEGER,
                    disponibleEnLatam BOOLEAN
                )
            """.trimIndent()

        // Crear la tabla Pelicula con clave foránea
        val scriptSQLCrearTablaPelicula =
            """
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

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun crearPlataformaStreaming(nombre: String, suscriptores: Int, precioMensual: Int, disponibleEnLatam: Boolean): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("suscriptores", suscriptores)
        valoresGuardar.put("precioMensual", precioMensual)
        valoresGuardar.put("disponibleEnLatam", disponibleEnLatam)
        val resultadoGuardar = baseDatosEscritura.insert("PlataformaStreaming", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

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
                    resultadoConsultaLectura.getInt(4) == 1 // disponibleEnLatam
                )
            )
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaPlataformas
    }
    fun eliminarPlataformaStreaming(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("PlataformaStreaming", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar != -1
    }
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
                    resultadoConsultaLectura.getInt(5) == 1, // esPopular
                )
            )
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return listaPeliculas
    }
    fun crearPelicula(titulo: String, genero: String, duracion: Double, fechaEstreno: String, esPopular: Boolean, idPlataforma: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("titulo", titulo)
        valoresGuardar.put("genero", genero)
        valoresGuardar.put("duracion", duracion)
        valoresGuardar.put("fechaEstreno", fechaEstreno)
        valoresGuardar.put("esPopular", esPopular)
        valoresGuardar.put("id_plataforma", idPlataforma)
        val resultadoGuardar = baseDatosEscritura.insert("Pelicula", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }
    fun eliminarPelicula(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("Pelicula", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar != -1
    }
}