package TP1

interface Criterio{

    fun criterioSegunTipo(unItinerario: Itinerario): Boolean

    fun nombre(): String
}

class Relajado(): Criterio{
    override fun criterioSegunTipo(unItinerario: Itinerario) = true
    override fun nombre() = "Relajado"
}

class Precavido(var unUsuario: Usuario): Criterio {
    override fun nombre() = "Precavido"
    override fun criterioSegunTipo(unItinerario: Itinerario) = unUsuario.conoceDestino(unItinerario.destino) or amigoConoceDestino(unItinerario)
    fun amigoConoceDestino(unItinerario: Itinerario) = unUsuario.amigos.any{it.conoceDestino(unItinerario.destino)}
}

class Localista(): Criterio{
    override fun nombre() = "Localista"
    override fun criterioSegunTipo(unItinerario: Itinerario) = unItinerario.destino.pais.equals(Destino.LOCAL, ignoreCase = true)
}

class Soñadores(var unUsuario: Usuario): Criterio{
    override fun nombre() = "Soñadores"
    override fun criterioSegunTipo(unItinerario: Itinerario) = unUsuario.estaEnDeseados(unItinerario) or unUsuario.destinoMasCaroQueDeseadoMasCaro(unItinerario)
}

class Activo(): Criterio {
    override fun nombre() = "Activo"
    override fun criterioSegunTipo(unItinerario: Itinerario) = unItinerario.todoLosDiasOcupados()
}

class Exigente(var dificultadPreferida: Int, var porcentajeDeseado: Int): Criterio {
    override fun nombre() = "Exigente"
    override fun criterioSegunTipo(unItinerario: Itinerario) = porcentajeSuficiente(unItinerario)

    fun cambiarDificultad(nuevaDificultad: Int) { dificultadPreferida = nuevaDificultad}

    fun cambiarPorcentaje(nuevaDificultad: Int){
        dificultadPreferida = nuevaDificultad
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) = unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado

}