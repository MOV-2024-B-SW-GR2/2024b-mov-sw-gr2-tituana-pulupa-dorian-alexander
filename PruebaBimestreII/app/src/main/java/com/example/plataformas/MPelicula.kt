package com.example.plataformas

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
        val popularidad = if (esPopular) "🌟 Popular" else "🔹 Normal"
        return """
            🎬 $titulo 
            📽️ Género: $genero
            ⏳ Duración: ${duracion} min
            🗓️ Estreno: $fechaEstreno
            $popularidad
        """.trimIndent()
    }
}