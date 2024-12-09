package models

import java.util.Date

data class Pelicula(
    val id: Int,
    var titulo: String,
    var genero: String,
    var duracion: Double, // En horas
    var fechaEstreno: Date,
    var esPopular: Boolean
)
