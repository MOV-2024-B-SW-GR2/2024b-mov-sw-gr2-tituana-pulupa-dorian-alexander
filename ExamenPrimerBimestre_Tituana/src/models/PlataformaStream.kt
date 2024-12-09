package models

data class PlataformaStream(
    val id: Int,
    var nombre: String,
    var suscriptores: Int,
    var precioMensual: Double,
    var disponibleEnLatam: Boolean,
    val peliculas: MutableList<Pelicula> = mutableListOf()
)
