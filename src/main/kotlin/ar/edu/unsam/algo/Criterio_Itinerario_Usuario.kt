package ar.edu.unsam.algo

interface CriterioItinerario{

    fun acepta(itinerario: Itinerario): Boolean
}

object Relajado: CriterioItinerario {
    override fun acepta(itinerario: Itinerario) = true
}

class Precavido(var usuario: Usuario): CriterioItinerario {

    override fun acepta(itinerario: Itinerario) = usuario.conoceDestino(itinerario.destino) || usuario.amigoConoceDestino(itinerario.destino)
}

object Localista: CriterioItinerario {

    override fun acepta(itinerario: Itinerario) = itinerario.tieneDestinoLocal()
}

class Soniador(var usuario: Usuario): CriterioItinerario {

    override fun acepta(itinerario: Itinerario) = usuario.estaEnDeseados(itinerario.destino) || usuario.destinoMasCaroQueDeseadoMasCaro(itinerario)
}

object Activo: CriterioItinerario {

    override fun acepta(itinerario: Itinerario) = itinerario.todoLosDiasOcupados()
}

class Exigente(private val dificultadPreferida: Dificultad, private val porcentajeDeseado: Int): CriterioItinerario {

    override fun acepta(itinerario: Itinerario) = porcentajeSuficiente(itinerario)

    private fun porcentajeSuficiente(itinerario: Itinerario) = itinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado
}