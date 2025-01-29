package com.example.prueba_tituana

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
                    descripcion VARCHAR(100),
                    suscriptores INTEGER,
                    precioMensual REAL,
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
                    fechaEstreno TEXT,
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

    fun crearPlataformaStreaming(nombre: String, descripcion: String, suscriptores: Int, precioMensual: Double, disponibleEnLatam: Boolean): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresGuardar = ContentValues()
        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("descripcion", descripcion)
        valoresGuardar.put("suscriptores", suscriptores)
        valoresGuardar.put("precioMensual", precioMensual)
        valoresGuardar.put("disponibleEnLatam", disponibleEnLatam)
        val resultadoGuardar = baseDatosEscritura.insert("PlataformaStreaming", null, valoresGuardar)
        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarPlataformaStreaming(id: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminar = baseDatosEscritura.delete("PlataformaStreaming", "id=?", parametrosConsultaDelete)
        baseDatosEscritura.close()
        return resultadoEliminar != -1
    }

    fun actualizarPlataformaStreaming(id: Int, nombre: String, descripcion: String, suscriptores: Int, precioMensual: Double, disponibleEnLatam: Boolean): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        valoresAActualizar.put("suscriptores", suscriptores)
        valoresAActualizar.put("precioMensual", precioMensual)
        valoresAActualizar.put("disponibleEnLatam", disponibleEnLatam)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update("PlataformaStreaming", valoresAActualizar, "id=?", parametrosConsultaActualizar)
        baseDatosEscritura.close()
        return resultadoActualizar != -1
    }

    fun consultarPlataformaStreamingPorId(id: Int): MPlataformaStreaming? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM PlataformaStreaming WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)
        if (resultadoConsultaLectura.moveToFirst()) {
            val plataforma = MPlataformaStreaming(
                resultadoConsultaLectura.getInt(0), // id
                resultadoConsultaLectura.getString(1), // nombre
                resultadoConsultaLectura.getInt(2), // suscriptores
                resultadoConsultaLectura.getDouble(3), // precioMensual
                resultadoConsultaLectura.getInt(4) == 1 // disponibleEnLatam
            )
            resultadoConsultaLectura.close()
            baseDatosLectura.close()
            return plataforma
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return null
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

    fun actualizarPelicula(id: Int, titulo: String, genero: String, duracion: Double, fechaEstreno: String, esPopular: Boolean, idPlataforma: Int): Boolean {
        val baseDatosEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("titulo", titulo)
        valoresAActualizar.put("genero", genero)
        valoresAActualizar.put("duracion", duracion)
        valoresAActualizar.put("fechaEstreno", fechaEstreno)
        valoresAActualizar.put("esPopular", esPopular)
        valoresAActualizar.put("id_plataforma", idPlataforma)
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizar = baseDatosEscritura.update("Pelicula", valoresAActualizar, "id=?", parametrosConsultaActualizar)
        baseDatosEscritura.close()
        return resultadoActualizar != -1
    }

    fun consultarPeliculaPorId(id: Int): MPelicula? {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = "SELECT * FROM Pelicula WHERE id = ?"
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)
        if (resultadoConsultaLectura.moveToFirst()) {
            val pelicula = MPelicula(
                resultadoConsultaLectura.getInt(0), // id
                resultadoConsultaLectura.getString(1), // titulo
                resultadoConsultaLectura.getString(2), // genero
                resultadoConsultaLectura.getDouble(3), // duracion
                java.sql.Date.valueOf(resultadoConsultaLectura.getString(4)), // fechaEstreno
                resultadoConsultaLectura.getInt(5) == 1 // esPopular
            )
            resultadoConsultaLectura.close()
            baseDatosLectura.close()
            return pelicula
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return null
    }

}