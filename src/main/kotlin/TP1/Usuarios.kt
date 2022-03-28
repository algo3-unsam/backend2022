package TP1

import java.time.LocalDate
import java.time.temporal.ChronoUnit

abstract class Usuario(
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


    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }

    fun esValido() =
        this.tienenInformacionCargadaEnLosStrings() and (this.fechaDeAlta > LocalDate.now()) and (this.diasParaViajar > 0)

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

    fun puedeRealizarItinerario(unItinerario: Itinerario) =
        this.diasSuficientes(unItinerario) and this.criterioSegunTipo(unItinerario)

    abstract fun criterioSegunTipo(unItinerario: Itinerario): Boolean

    fun diasSuficientes(unItinerario: Itinerario) = diasParaViajar >= unItinerario.cantDias
}


class UsuarioRelajado(
    nombre: String,
    apellido: String,
    username: String,
    fechaDeAlta: LocalDate,
    paisDeResidencia: String,
    diasParaViajar: Int,
    amigos: MutableList<Usuario> = mutableListOf(),
    destinosDeseados: MutableList<Destino> = mutableListOf(),
    destinosVisitados: MutableList<Destino> = mutableListOf()
) : Usuario(
    nombre,
    apellido,
    username,
    fechaDeAlta,
    paisDeResidencia,
    diasParaViajar,
    amigos,
    destinosDeseados,
    destinosVisitados
) {
    override fun criterioSegunTipo(unItinerario: Itinerario) = true
}

class UsuarioPrecavido(
    nombre: String,
    apellido: String,
    username: String,
    fechaDeAlta: LocalDate,
    paisDeResidencia: String,
    diasParaViajar: Int,
    amigos: MutableList<Usuario> = mutableListOf(),
    destinosDeseados: MutableList<Destino> = mutableListOf(),
    destinosVisitados: MutableList<Destino> = mutableListOf()
) : Usuario(
    nombre,
    apellido,
    username,
    fechaDeAlta,
    paisDeResidencia,
    diasParaViajar,
    amigos,
    destinosDeseados,
    destinosVisitados
) {
    override fun criterioSegunTipo(unItinerario: Itinerario) =
        this.conoceDestino(unItinerario.destino) or amigoConoceDestino(unItinerario)

    fun amigoConoceDestino(unItinerario: Itinerario) = amigos.any { it.conoceDestino(unItinerario.destino) }
}

class UsuarioLocalista(
    nombre: String,
    apellido: String,
    username: String,
    fechaDeAlta: LocalDate,
    paisDeResidencia: String,
    diasParaViajar: Int,
    amigos: MutableList<Usuario> = mutableListOf(),
    destinosDeseados: MutableList<Destino> = mutableListOf(),
    destinosVisitados: MutableList<Destino> = mutableListOf()
) : Usuario(
    nombre,
    apellido,
    username,
    fechaDeAlta,
    paisDeResidencia,
    diasParaViajar,
    amigos,
    destinosDeseados,
    destinosVisitados
) {
    override fun criterioSegunTipo(unItinerario: Itinerario) = unItinerario.destino.pais == Destino.LOCAL
}

class UsuarioSoñadores(
    nombre: String,
    apellido: String,
    username: String,
    fechaDeAlta: LocalDate,
    paisDeResidencia: String,
    diasParaViajar: Int,
    amigos: MutableList<Usuario> = mutableListOf(),
    destinosDeseados: MutableList<Destino> = mutableListOf(),
    destinosVisitados: MutableList<Destino> = mutableListOf()
) : Usuario(
    nombre,
    apellido,
    username,
    fechaDeAlta,
    paisDeResidencia,
    diasParaViajar,
    amigos,
    destinosDeseados,
    destinosVisitados
) {
    override fun criterioSegunTipo(unItinerario: Itinerario) =
        this.estaEnDeseados(unItinerario) or this.destinoMasCaroQueDeseadoMasCaro(unItinerario)

    fun estaEnDeseados(unItinerario: Itinerario) = destinosDeseados.contains(unItinerario.destino)

    fun deseadoMasCaro() = destinosDeseados.maxOf { it.costoBase }

    fun destinoMasCaroQueDeseadoMasCaro(unItinerario: Itinerario) =
        unItinerario.destino.costoBase > this.deseadoMasCaro()

}

class UsuarioActivo(
    nombre: String,
    apellido: String,
    username: String,
    fechaDeAlta: LocalDate,
    paisDeResidencia: String,
    diasParaViajar: Int,
    amigos: MutableList<Usuario> = mutableListOf(),
    destinosDeseados: MutableList<Destino> = mutableListOf(),
    destinosVisitados: MutableList<Destino> = mutableListOf()
) : Usuario(
    nombre,
    apellido,
    username,
    fechaDeAlta,
    paisDeResidencia,
    diasParaViajar,
    amigos,
    destinosDeseados,
    destinosVisitados
) {
    override fun criterioSegunTipo(unItinerario: Itinerario) = unItinerario.todoLosDiasOcupados()
}

class UsuarioExigente(
    nombre: String,
    apellido: String,
    username: String,
    fechaDeAlta: LocalDate,
    paisDeResidencia: String,
    diasParaViajar: Int,
    amigos: MutableList<Usuario> = mutableListOf(),
    destinosDeseados: MutableList<Destino> = mutableListOf(),
    destinosVisitados: MutableList<Destino> = mutableListOf()
) : Usuario(
    nombre,
    apellido,
    username,
    fechaDeAlta,
    paisDeResidencia,
    diasParaViajar,
    amigos,
    destinosDeseados,
    destinosVisitados
) {
    var dificultadPreferida = 0
    var porcentajeDeseado = 0

    override fun criterioSegunTipo(unItinerario: Itinerario) = porcentajeSuficiente(unItinerario)

    fun cambiarDificultad(nuevaDificultad: Int) {
        dificultadPreferida = nuevaDificultad
    }

    fun cambiarPorcentaje(nuevaDificultad: Int) {
        dificultadPreferida = nuevaDificultad
    }

    fun porcentajeSuficiente(unItinerario: Itinerario) =
        unItinerario.porcentajeDeActividadXDificultad(dificultadPreferida) >= porcentajeDeseado

}