package ar.edu.unsam.algo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ServiceDestino {
    val destinos = JsonDestinosBuilder.listaDestinos()
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    lateinit var repositorio : Repositorio<Destino>

    /*fun add(destino: Destino){
        destinos.add(destino)
    }*/

    fun actualizarRepo(){
        destinos.forEach { (repositorio.crearOModificar(it)) }
    }

    fun getDestinos(): String{
        return gsonPretty.toJson(destinos)
    }

    fun printJson(){
        println(getDestinos())
    }
}

object JsonDestinosBuilder{
    val destino1JSON = "{\"id\":2,\"pais\":\"Argentina\",\"ciudad\":\"Buenos Aires\",\"costoBase\":10000}"
    val destino2JSON = "{\"pais\":\"Malasia\",\"ciudad\":\"Kuala Lumpur\",\"costoBase\":10000}"
    val destinosJSON = "[$destino1JSON,$destino2JSON]"
    val gson = Gson()
    val tipoListaDestinos: Type = object : TypeToken<List<Destino?>?>() {}.getType()
    val destinos: List<Destino> = gson.fromJson<List<Destino>>(destinosJSON, tipoListaDestinos)

    fun listaDestinos() = destinos

}