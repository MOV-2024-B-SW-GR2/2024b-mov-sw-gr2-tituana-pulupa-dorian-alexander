package persistencia

import models.Pelicula
import models.PlataformaStream
import java.io.File
import java.text.SimpleDateFormat

class PersistenciaTXT(private val filePath: String) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    fun guardarPlataformas(plataformas: List<PlataformaStream>) {
        File(filePath).bufferedWriter().use { writer ->
            plataformas.forEach { plataforma ->
                writer.write("PLATAFORMA|${plataforma.id}|${plataforma.nombre}|${plataforma.suscriptores}|${plataforma.precioMensual}|${plataforma.disponibleEnLatam}\n")
                plataforma.peliculas.forEach { pelicula ->
                    writer.write("PELICULA|${pelicula.id}|${pelicula.titulo}|${pelicula.genero}|${pelicula.duracion}|${dateFormat.format(pelicula.fechaEstreno)}|${pelicula.esPopular}\n")
                }
            }
        }
    }

    fun cargarPlataformas(): MutableList<PlataformaStream> {
        val plataformas = mutableListOf<PlataformaStream>()
        val archivo = File(filePath)
        if (!archivo.exists()) return plataformas

        var plataformaActual: PlataformaStream? = null
        archivo.forEachLine { linea ->
            val partes = linea.split("|")
            when (partes[0]) {
                "PLATAFORMA" -> {
                    plataformaActual = PlataformaStream(
                        id = partes[1].toInt(),
                        nombre = partes[2],
                        suscriptores = partes[3].toInt(),
                        precioMensual = partes[4].toDouble(),
                        disponibleEnLatam = partes[5].toBoolean(),
                        peliculas = mutableListOf()  // Inicializar la lista de películas
                    )
                    plataformas.add(plataformaActual!!)
                }
                "PELICULA" -> {
                    plataformaActual?.let { plataforma ->
                        val pelicula = Pelicula(
                            id = partes[1].toInt(),
                            titulo = partes[2],
                            genero = partes[3],
                            duracion = partes[4].toDouble(),
                            fechaEstreno = dateFormat.parse(partes[5]),
                            esPopular = partes[6].toBoolean()
                        )
                        plataforma.peliculas.add(pelicula)
                    }
                }
            }
        }
        return plataformas
    }

    // Método para mostrar las plataformas y películas de una forma más amigable
    fun mostrarPlataformas(plataformas: List<PlataformaStream>) {
        plataformas.forEach { plataforma ->
            println("=======================================")
            println("PLATAFORMA: ${plataforma.nombre}")
            println("ID: ${plataforma.id}")
            println("Suscriptores: ${plataforma.suscriptores}")
            println("Precio Mensual: $${plataforma.precioMensual}")
            println("Disponible en LATAM: ${if (plataforma.disponibleEnLatam) "Sí" else "No"}")
            println("Películas:")

            if (plataforma.peliculas.isEmpty()) {
                println("  No hay películas disponibles.")
            } else {
                plataforma.peliculas.forEach { pelicula ->
                    println("  - Título: ${pelicula.titulo}")
                    println("    Género: ${pelicula.genero}")
                    println("    Duración: ${pelicula.duracion} minutos")
                    println("    Fecha de Estreno: ${dateFormat.format(pelicula.fechaEstreno)}")
                    println("    Popularidad: ${if (pelicula.esPopular) "Sí" else "No"}")
                    println("    -----------------------------------")
                }
            }
            println("=======================================")
        }
    }
}
