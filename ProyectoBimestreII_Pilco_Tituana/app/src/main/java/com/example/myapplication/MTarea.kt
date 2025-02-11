package com.example.myapplication

class MTarea(
    var id: Int = 0,
    var descripcion: String = "",
    var materia: String = "",
    var fecha_entrega: String = "",
    var estado: Boolean = false,
) {

    override fun toString(): String {
        val estadoTexto = if (estado) "Hecho" else "Pendiente"
        return """
        📝 Tarea Detalles:
        ----------------------
        🆔 ID:                $id
        🖊 Descripción:       '$descripcion'
        📚 Materia:          '$materia'
        📅 Fecha de Entrega: '$fecha_entrega'
        ✅ Estado:           $estadoTexto
    """.trimIndent()
    }

}
