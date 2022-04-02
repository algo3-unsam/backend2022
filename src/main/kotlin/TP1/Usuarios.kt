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
    var destinosVisitados: MutableList<Destino> = mutableListOf()
) {

    lateinit var criterio: Criterio

    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }

    fun esValido() =
        this.tienenInformacionCargadaEnLosStrings() and (this.fechaDeAlta > LocalDate.now()) and (this.diasParaViajar > 0)

    fun cambiarCriterio(unCriterio: Criterio) {
        criterio = unCriterio
    }

    fun tienenInformacionCargadaEnLosStrings() =
        !(this.nombre.isNullOrEmpty() and this.apellido.isNullOrEmpty() and this.username.isNullOrEmpty() and this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(unUsuario: Usuario) = amigos.add(unUsuario)

    fun itinerariosAPuntuar(listaDeItirenarios: MutableList<Itinerario>) =
        listaDeItirenarios.filter { !it.sosMiCreador(this) and this.conoceDestino(it.destino) }

    fun conoceDestino(unDestino: Destino) =
        (destinosDeseados.contains(unDestino) or destinosVisitados.contains(unDestino))

    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())

    fun descuentoPorAntiguedad() = if (antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(unDestino: Destino) = this.paisDeResidencia == unDestino.pais


    fun amigoConoceDestino(unItinerario: Itinerario) = amigos.any { it.conoceDestino(unItinerario.destino) }


    fun estaEnDeseados(unItinerario: Itinerario) = destinosDeseados.contains(unItinerario.destino)

    fun deseadoMasCaro() = destinosDeseados.maxOf { it.costoBase }

    fun destinoMasCaroQueDeseadoMasCaro(unItinerario: Itinerario) =
        unItinerario.destino.costoBase > this.deseadoMasCaro()

    fun puedeRealizarItinerario(unItinerario: Itinerario) =
        this.diasSuficientes(unItinerario) and criterio.criterioSegunTipo(unItinerario, this)

    fun diasSuficientes(unItinerario: Itinerario) = diasParaViajar >= unItinerario.cantDias
}