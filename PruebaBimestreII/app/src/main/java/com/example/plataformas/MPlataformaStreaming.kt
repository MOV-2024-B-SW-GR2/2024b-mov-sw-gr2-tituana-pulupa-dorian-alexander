package com.example.plataformas

import com.google.android.gms.maps.model.LatLng

class MPlataformaStreaming(
    val id: Int,
    var nombre: String?,
    var suscriptores: Int,
    var precioMensual: Double,
    var disponibleEnLatam: Boolean,
    val direccion: LatLng
) {
    override fun toString(): String {
        val disponibilidad = if (disponibleEnLatam) "🌎 Disponible en LATAM" else "❌ No disponible en LATAM"

        return """
        📺 Plataforma de Streaming
        --------------------------------
        🆔 ID:              $id
        🎬 Nombre:         ${nombre ?: "Desconocido"}
        👥 Suscriptores:   $suscriptores
        💰 Precio Mensual: $${"%.2f".format(precioMensual)}
        🌍 Disponibilidad: $disponibilidad
           Ubicación: ${direccion.latitude}, ${direccion.longitude}  
        """.trimIndent()
    }
}