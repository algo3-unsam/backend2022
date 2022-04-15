package ar.edu.unsam.algo

interface Criterio{

    fun acepta(itinerario: Itinerario): Boolean
}

object Relajado: Criterio {
    override fun acepta(itinerario: Itinerario) = true
}

class Precavido(var unUsuario: Usuario): Criterio {

    override fun acepta(itinerario: Itinerario) = unUsuario.conoceDestino(itinerario.destino) || unUsuario.amigoConoceDestino(itinerario.destino)
}

object Localista: Criterio {

    override fun acepta(itinerario: Itinerario) = itinerario.tieneDestinoLocal()
}

class Soniador(var unUsuario: Usuario): Criterio {

    override fun acepta(itinerario: Itinerario) = unUsuario.estaEnDeseados(itinerario.destino) || unUsuario.destinoMasCaroQueDeseadoMasCaro(itinerario)
}

object Activo: Criterio {

    override fun acepta(itinerario: Itinerario) = itinerario.todoLosDiasOcupados()
}

class Exigente(var dificultadPreferida: Dificultad, var porcentajeDeseado: Int): Criterio {

    override fun acepta(itinerario: Itinerario) = porcentajeSuficiente(itinerario)

    fun cambiarDificultad(nuevaDificultad: Dificultad) { dificultadPreferida = nuevaDificultad}

    fun cambiarPorcentaje(nuevaPorcentaje: Int){
        porcentajeDeseado = nuevaPorcentaje
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) = unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado
}