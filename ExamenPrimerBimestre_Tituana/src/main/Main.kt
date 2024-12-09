package main

import models.Pelicula
import models.PlataformaStream
import persistencia.PersistenciaTXT
import java.util.*
import java.text.SimpleDateFormat

val persistencia = PersistenciaTXT("plataformas.txt")
val plataformas = persistencia.cargarPlataformas()

fun main() {
    var opcion: Int
    do {
        println("\n--- Menú Principal ---")
        println("1. Crear Plataforma")
        println("2. Leer Plataformas")
        println("3. Actualizar Plataforma")
        println("4. Eliminar Plataforma")
        println("5. Gestionar Películas")
        println("6. Salir")
        print("Selecciona una opción: ")
        opcion = readLine()?.toIntOrNull() ?: 0

        when (opcion) {
            1 -> crearPlataforma()
            2 -> leerPlataformas()
            3 -> actualizarPlataforma()
            4 -> eliminarPlataforma()
            5 -> gestionarPeliculas()
            6 -> {
                persistencia.guardarPlataformas(plataformas)
                println("Datos guardados en 'plataformas.txt'.")
                println("¡Hasta luego!")
            }
            else -> println("Opción inválida. Intenta nuevamente.")
        }
    } while (opcion != 6)
}

fun crearPlataforma() {
    print("\nNombre: ")
    val nombre = readLine() ?: ""
    print("Número de suscriptores: ")
    val suscriptores = readLine()?.toIntOrNull() ?: 0
    print("Precio mensual: ")
    val precioMensual = readLine()?.toDoubleOrNull() ?: 0.0
    print("¿Disponible en LATAM? (true/false): ")
    val disponibleEnLatam = readLine()?.toBoolean() ?: false

    val plataforma = PlataformaStream(
        id = plataformas.size + 1,
        nombre = nombre,
        suscriptores = suscriptores,
        precioMensual = precioMensual,
        disponibleEnLatam = disponibleEnLatam
    )
    plataformas.add(plataforma)
    println("Plataforma creada exitosamente.")
}

fun leerPlataformas() {
    if (plataformas.isEmpty()) {
        println("No hay plataformas registradas.")
    } else {
        persistencia.mostrarPlataformas(plataformas)
    }
}

fun actualizarPlataforma() {
    print("\nID de la plataforma a actualizar: ")
    val id = readLine()?.toIntOrNull()
    val plataforma = plataformas.find { it.id == id }

    if (plataforma != null) {
        print("Nuevo nombre (actual: ${plataforma.nombre}): ")
        plataforma.nombre = readLine() ?: plataforma.nombre
        print("Nuevo número de suscriptores (actual: ${plataforma.suscriptores}): ")
        plataforma.suscriptores = readLine()?.toIntOrNull() ?: plataforma.suscriptores
        print("Nuevo precio mensual (actual: ${plataforma.precioMensual}): ")
        plataforma.precioMensual = readLine()?.toDoubleOrNull() ?: plataforma.precioMensual
        print("¿Nuevo estado de disponibilidad en LATAM? (actual: ${plataforma.disponibleEnLatam}): ")
        plataforma.disponibleEnLatam = readLine()?.toBoolean() ?: plataforma.disponibleEnLatam
        println("Plataforma actualizada.")
    } else {
        println("Plataforma no encontrada.")
    }
}

fun eliminarPlataforma() {
    print("\nID de la plataforma a eliminar: ")
    val id = readLine()?.toIntOrNull()
    val plataforma = plataformas.find { it.id == id }

    if (plataforma != null) {
        plataformas.remove(plataforma)
        println("Plataforma eliminada.")
    } else {
        println("Plataforma no encontrada.")
    }
}

fun gestionarPeliculas() {
    print("\nID de la plataforma para gestionar películas: ")
    val id = readLine()?.toIntOrNull()
    val plataforma = plataformas.find { it.id == id }

    if (plataforma != null) {
        var opcionPeliculas: Int
        do {
            println("\n--- Menú de Gestión de Películas ---")
            println("1. Añadir Película")
            println("2. Ver Películas")
            println("3. Volver")
            print("Selecciona una opción: ")
            opcionPeliculas = readLine()?.toIntOrNull() ?: 0

            when (opcionPeliculas) {
                1 -> añadirPelicula(plataforma)
                2 -> verPeliculas(plataforma)
                3 -> println("Volviendo al menú principal...")
                else -> println("Opción inválida. Intenta nuevamente.")
            }
        } while (opcionPeliculas != 3)
    } else {
        println("Plataforma no encontrada.")
    }
}

fun añadirPelicula(plataforma: PlataformaStream) {
    print("\nTítulo de la película: ")
    val titulo = readLine() ?: ""
    print("Género: ")
    val genero = readLine() ?: ""
    print("Duración en horas: ")
    val duracion = readLine()?.toDoubleOrNull() ?: 0.0
    print("Fecha de estreno (dd/MM/yyyy): ")
    val fechaEstreno = readLine()?.let { SimpleDateFormat("dd/MM/yyyy").parse(it) } ?: Date()
    print("¿Es popular? (true/false): ")
    val esPopular = readLine()?.toBoolean() ?: false

    val pelicula = Pelicula(
        id = plataforma.peliculas.size + 1,
        titulo = titulo,
        genero = genero,
        duracion = duracion,
        fechaEstreno = fechaEstreno,
        esPopular = esPopular
    )
    plataforma.peliculas.add(pelicula)
    println("Película añadida exitosamente.")
}

fun verPeliculas(plataforma: PlataformaStream) {
    if (plataforma.peliculas.isEmpty()) {
        println("No hay películas registradas en esta plataforma.")
    } else {
        plataforma.peliculas.forEach { println(it) }
    }
}
