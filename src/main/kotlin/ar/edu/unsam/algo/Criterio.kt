package ar.edu.unsam.algo

interface Criterio{

    fun acepta(unItinerario: Itinerario): Boolean

}

object Relajado: Criterio {
    override fun acepta(unItinerario: Itinerario) = true

}

class Precavido(var unUsuario: Usuario): Criterio {

    override fun acepta(unItinerario: Itinerario) = unUsuario.conoceDestino(unItinerario.destino) || unUsuario.amigoConoceDestino(unItinerario.destino)

}

object Localista: Criterio {

    override fun acepta(unItinerario: Itinerario) = unItinerario.tieneDestinoLocal()
}

class Soniador(var unUsuario: Usuario): Criterio {

    override fun acepta(unItinerario: Itinerario) = unUsuario.estaEnDeseados(unItinerario.destino) || unUsuario.destinoMasCaroQueDeseadoMasCaro(unItinerario)
}

object Activo: Criterio {

    override fun acepta(unItinerario: Itinerario) = unItinerario.todoLosDiasOcupados()
}

class Exigente(var dificultadPreferida: Dificultad, var porcentajeDeseado: Int): Criterio {

    override fun acepta(unItinerario: Itinerario) = porcentajeSuficiente(unItinerario)

    fun cambiarDificultad(nuevaDificultad: Dificultad) { dificultadPreferida = nuevaDificultad}

    fun cambiarPorcentaje(nuevaPorcentaje: Int){
        porcentajeDeseado = nuevaPorcentaje
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) = unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado

}