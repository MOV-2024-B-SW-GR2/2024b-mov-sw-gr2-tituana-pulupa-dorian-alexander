package com.example.prueba_tituana

import java.util.Date

class MPelicula(
    val id: Int,
    var titulo: String,
    var genero: String,
    var duracion: Double, // En horas
    var fechaEstreno: Date,
    var esPopular: Boolean
)
{
}