package com.example.prueba_tituana

class MPlataformaStreaming(
    val id: Int,
    var nombre: String?,
    var suscriptores: Int,
    var precioMensual: Double,
    var disponibleEnLatam: Boolean,
    val peliculas: MutableList<MPelicula> = mutableListOf()
) {
    override fun toString(): String {
        return "$nombre"
    }
}