package TP1

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    var nombre: String,
    var apellido: String,
    var username: String,
    var fechaDeAlta: LocalDate,
    var paisDeResidencia: String,
    var diasParaViajar: Int,
    var amigos: MutableList<Usuario> = mutableListOf(),
    var destinosDeseados: MutableList<Destino> = mutableListOf(),
    var destinosVisitados: MutableList<Destino> = mutableListOf(),
    var criterio: Criterio){

    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }

    fun esValido() = this.tienenInformacionCargadaEnLosStrings() and (this.fechaDeAlta > LocalDate.now()) and (this.diasParaViajar > 0)

    fun cambiarCriterio(unCriterio: Criterio){
        criterio = unCriterio
    }

    fun tienenInformacionCargadaEnLosStrings() = !(this.nombre.isNullOrEmpty() and this.apellido.isNullOrEmpty() and this.username.isNullOrEmpty() and this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(unUsuario: Usuario) = amigos.add(unUsuario)

    fun itinerariosAPuntuar(listaDeItirenarios: MutableList<Itinerario>)= listaDeItirenarios.filter{ !it.sosMiCreador(this) and this.conoceDestino(it.destino)}

    fun conoceDestino(unDestino: Destino) = (destinosDeseados.contains(unDestino) or destinosVisitados.contains(unDestino))

    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())

    fun descuentoPorAntiguedad() = if (antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(unDestino: Destino) = this.paisDeResidencia == unDestino.pais

    fun estaEnDeseados(unItinerario: Itinerario) = destinosDeseados.contains(unItinerario.destino)

    fun deseadoMasCaro() = destinosDeseados.maxOf{it.costoBase}

    fun destinoMasCaroQueDeseadoMasCaro(unItinerario: Itinerario) = unItinerario.destino.costoBase > this.deseadoMasCaro()

    fun puedeRealizarItinerario(unItinerario: Itinerario) = this.diasSuficientes(unItinerario) and criterio.criterioSegunTipo(unItinerario, this)

    fun diasSuficientes(unItinerario: Itinerario) = diasParaViajar >= unItinerario.cantDias
}

abstract class Criterio(){
    abstract fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario): Boolean
}

class Relajado(): Criterio(){
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = true
}

class Precavido(): Criterio() {
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = unUsuario.conoceDestino(unItinerario.destino) or amigoConoceDestino(unItinerario,unUsuario)
    fun amigoConoceDestino(unItinerario: Itinerario, unUsuario: Usuario) = unUsuario.amigos.any{it.conoceDestino(unItinerario.destino)}
}

class Localista(): Criterio(){
    override fun criterioSegunTipo(unItinerario: Itinerario,unUsuario: Usuario) = unItinerario.destino.pais == Destino.LOCAL
}

class Soñadores(): Criterio(){
    override fun criterioSegunTipo(unItinerario: Itinerario,unUsuario: Usuario) = unUsuario.estaEnDeseados(unItinerario) or unUsuario.destinoMasCaroQueDeseadoMasCaro(unItinerario)
}

class Activo(): Criterio() {
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = unItinerario.todoLosDiasOcupados()
}

class Exigente(var dificultadPreferida: Int, var porcentajeDeseado: Int): Criterio() {
    override fun criterioSegunTipo(unItinerario: Itinerario, unUsuario: Usuario) = porcentajeSuficiente(unItinerario)

    fun cambiarDificultad(nuevaDificultad: Int) { dificultadPreferida = nuevaDificultad}

    fun cambiarPorcentaje(nuevaDificultad: Int){
        dificultadPreferida = nuevaDificultad
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) = unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado

}