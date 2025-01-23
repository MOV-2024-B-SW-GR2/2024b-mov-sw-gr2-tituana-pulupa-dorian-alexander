package com.example.prueba_tituana

class BaseDeDatosMemoria {
    companion object{
        var arregloMPlataformaStreaming = arrayListOf<MPlataformaStreaming>()
        init {
            arregloMPlataformaStreaming.add(MPlataformaStreaming(1, "Netflix", 100000000, 199.99, true))
            arregloMPlataformaStreaming.add(MPlataformaStreaming(2, "Amazon Prime Video", 150000000, 149.99, true))
            arregloMPlataformaStreaming.add(MPlataformaStreaming(3, "Disney+", 70000000, 229.99, true))
            arregloMPlataformaStreaming.add(MPlataformaStreaming(4, "HBO Max", 40000000, 249.99, true))
        }
    }

}