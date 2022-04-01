package TP1

interface Criterio{
    fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario): Boolean
}

class Relajado(): Criterio{
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = true
}

class Precavido(): Criterio {
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = unUsuario.conoceDestino(unItinerario.destino) or amigoConoceDestino(unItinerario,unUsuario)
    fun amigoConoceDestino(unItinerario: Itinerario, unUsuario: Usuario) = unUsuario.amigos.any{it.conoceDestino(unItinerario.destino)}
}

class Localista(): Criterio{
    override fun criterioSegunTipo(unItinerario: Itinerario,unUsuario: Usuario) = unItinerario.destino.pais == Destino.LOCAL
}

class Soñadores(): Criterio{
    override fun criterioSegunTipo(unItinerario: Itinerario,unUsuario: Usuario) = unUsuario.estaEnDeseados(unItinerario) or unUsuario.destinoMasCaroQueDeseadoMasCaro(unItinerario)
}

class Activo(): Criterio {
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = unItinerario.todoLosDiasOcupados()
}

class Exigente(var dificultadPreferida: Int, var porcentajeDeseado: Int): Criterio {
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = porcentajeSuficiente(unItinerario)

    fun cambiarDificultad(nuevaDificultad: Int) { dificultadPreferida = nuevaDificultad}

    fun cambiarPorcentaje(nuevaDificultad: Int){
        dificultadPreferida = nuevaDificultad
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) = unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado

}