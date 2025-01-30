package com.example.examenbimestreii

class MPlataformaStreaming(
    val id: Int,
    var nombre: String?,
    var suscriptores: Int,
    var precioMensual: Double,
    var disponibleEnLatam: Boolean
) {
    override fun toString(): String {
        return "$id $nombre"
    }
}