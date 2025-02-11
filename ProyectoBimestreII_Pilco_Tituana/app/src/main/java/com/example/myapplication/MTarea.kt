package com.example.myapplication

class MTarea(
    var id: Int = 0,
    var descripcion: String = "",
    var materia: String = "",
    var fecha_entrega: String = "",
    var estado: Boolean = false,
) {

    override fun toString(): String {
        // Usamos el check (✔) para tareas realizadas y la X (❌) para tareas pendientes
        val estadoIcono = if (estado) "✔" else "❌"
        val estadoTexto = if (estado) "Realizada" else "Pendiente"

        return """
        📝 Tarea Detalles:
        ----------------------
        🆔 ID:                $id
        🖊 Descripción:       '$descripcion'
        📚 Materia:          '$materia'
        📅 Fecha de Entrega: '$fecha_entrega'
        $estadoIcono Estado: $estadoTexto
    """.trimIndent()
    }

}
