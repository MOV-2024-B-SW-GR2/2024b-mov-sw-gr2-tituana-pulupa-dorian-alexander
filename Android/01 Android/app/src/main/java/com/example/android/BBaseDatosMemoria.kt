package com.example.android

class BBaseDatosMemoria {
    companion object{
        var arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Dorian","dd@dd.com"))
            arregloBEntrenador.add(BEntrenador(2,"Emily","ee@ee.com"))
            arregloBEntrenador.add(BEntrenador(3,"Lenin","ll@ll.com"))
        }
    }
}