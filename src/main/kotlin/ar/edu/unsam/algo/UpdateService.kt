package ar.edu.unsam.algo

import com.google.gson.GsonBuilder

class ServiceDestino {
    val destinos = mutableListOf<Destino>()
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    fun add(destino: Destino){
        destinos.add(destino)
    }

    fun printJson(){
        println(gsonPretty.toJson(destinos))
    }
}
