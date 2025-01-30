package com.example.examenbimestreii

class MPelicula(
    val id: Int,
    var titulo: String,
    var genero: String,
    var duracion: Double,
    var fechaEstreno: String,
    var esPopular: Boolean
)
{
    override fun toString(): String {
        return "$id - $titulo -$genero"
    }
}