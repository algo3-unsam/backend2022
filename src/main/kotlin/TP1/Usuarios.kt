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
    var destinosVisitados: MutableList<Destino> = mutableListOf()) {
    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }

    fun itinerariosAPuntuar(listaDeItirenarios: MutableList<Itinerario>)= listaDeItirenarios.filter{ it.sosMiCreador(this) and this.conoceDestino(it.destino)}



    fun conoceDestino(unDestino: Destino) = (destinosDeseados.contains(unDestino) or destinosVisitados.contains(unDestino))

    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())//Period.between(LocalDate.parse(fechaDeAlta), LocalDate.now())

    fun descuentoPorAntiguedad() = if (antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(unDestino: Destino) = this.paisDeResidencia == unDestino.pais
}


