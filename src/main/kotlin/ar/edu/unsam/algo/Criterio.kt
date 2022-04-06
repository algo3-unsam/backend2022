package ar.edu.unsam.algo

interface Criterio{

    fun criterioSegunTipo(unItinerario: Itinerario): Boolean

}

object Relajado: Criterio {
    override fun criterioSegunTipo(unItinerario: Itinerario) = true

}

class Precavido(var unUsuario: Usuario): Criterio {

    override fun criterioSegunTipo(unItinerario: Itinerario) = unUsuario.conoceDestino(unItinerario.destino) || amigoConoceDestino(unItinerario)
    fun amigoConoceDestino(unItinerario: Itinerario) = unUsuario.amigos.any{it.conoceDestino(unItinerario.destino)}
}

object Localista: Criterio {

    override fun criterioSegunTipo(unItinerario: Itinerario) = unItinerario.destino.pais.equals(Destino.LOCAL, ignoreCase = true)
}

class Soniadores(var unUsuario: Usuario): Criterio {

    override fun criterioSegunTipo(unItinerario: Itinerario) = unUsuario.estaEnDeseados(unItinerario) || unUsuario.destinoMasCaroQueDeseadoMasCaro(unItinerario)
}

object Activo: Criterio {

    override fun criterioSegunTipo(unItinerario: Itinerario) = unItinerario.todoLosDiasOcupados()
}

class Exigente(var dificultadPreferida: Dificultades, var porcentajeDeseado: Int): Criterio {

    override fun criterioSegunTipo(unItinerario: Itinerario) = porcentajeSuficiente(unItinerario)

    fun cambiarDificultad(nuevaDificultad: Dificultades) { dificultadPreferida = nuevaDificultad}

    fun cambiarPorcentaje(nuevaPorcentaje: Int){
        porcentajeDeseado = nuevaPorcentaje
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) = unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado

}