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

    fun tieneDestinoSoñado() = destinosDeseados.size > 0

    fun esValido(){
        if(!this.tienenInformacionCargadaEnLosStrings() or (this.fechaDeAlta > LocalDate.now()) or (this.diasParaViajar < 0) or (!this.tieneDestinoSoñado())){
            throw Exception("Hay informacion vacia")
        }
    }

    fun cambiarCriterio(unCriterio: Criterio) {
        criterio = unCriterio
    }

    fun tienenInformacionCargadaEnLosStrings() =
        !(this.nombre.isNullOrEmpty() and this.apellido.isNullOrEmpty() and this.username.isNullOrEmpty() and this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(unUsuario: Usuario) = amigos.add(unUsuario)

    fun consultarPuntaje(unItinerario: Itinerario) = unItinerario.verPuntaje(this)


    fun puedoPuntuar(unItinerario: Itinerario) = !((unItinerario.sosMiCreador(this)) or (this.yaPuntee(unItinerario))) and this.conoceDestino(unItinerario.destino)

    fun yaPuntee(unItinerario: Itinerario) = unItinerario.puntajes.containsKey(this.username)

    fun puntuar(unItinerario: Itinerario, puntaje: Int){
        if((puntaje<1) or (puntaje>10)){
            throw Exception("El puntaje tiene que ser del 1 al 10")
        }
        else if(!puedoPuntuar(unItinerario)){
                throw Exception("No puedo puntuar")
        }
        else {
            unItinerario.darPuntaje(this, puntaje)
        }
    }

    fun conoceDestino(unDestino: Destino) =
        (destinosDeseados.contains(unDestino) or destinosVisitados.contains(unDestino))

    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())

    fun descuentoPorAntiguedad() = if (antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(unDestino: Destino) = this.paisDeResidencia.equals(unDestino.pais, ignoreCase = true)

    fun estaEnDeseados(unItinerario: Itinerario) = destinosDeseados.contains(unItinerario.destino)

    fun deseadoMasCaro() = destinosDeseados.maxOf { it.costoBase }

    fun destinoMasCaroQueDeseadoMasCaro(unItinerario: Itinerario) =
        unItinerario.destino.costoBase > this.deseadoMasCaro()

    fun puedeRealizarItinerario(unItinerario: Itinerario) =
        this.diasSuficientes(unItinerario) and criterio.criterioSegunTipo(unItinerario)

    fun diasSuficientes(unItinerario: Itinerario) = diasParaViajar >= unItinerario.cantDias
}