package ar.edu.unsam.algo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

interface ServiceDestino {
    fun getDestinos(): String
}

object ActualizadorDeDestinos {
    lateinit var serviceDestino: ServiceDestino
    lateinit var repositorio: Repositorio<Destino>

    fun deJsonAListaDestino():List<Destino>{
        val gson = Gson()
        val tipoListaDestinos: Type = object : TypeToken<List<Destino?>?>() {}.getType()
       return gson.fromJson<List<Destino>>(serviceDestino.getDestinos(), tipoListaDestinos)
    }

    fun actualizarDestinos(){
        deJsonAListaDestino().forEach{repositorio.crearOModificar(it)}
    }
}

