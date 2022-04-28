package ar.edu.unsam.algo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
val mapper = jacksonObjectMapper()

class Json(val destinos: MutableList<Destino> = mutableListOf()) {

    fun agregarDestino(destino: Destino) {
        destinos.add(destino)
    }

    fun contieneDestinos(destino: Destino) = destinos.contains(destino)

    val destinoJson = mapper.writeValueAsString(destinos)
}

//var destino = Destino("Argentina", "BuenosAires", 10000f)
//val destinoJson = mapper.writeValueAsString(destino)