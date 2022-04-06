package ar.edu.unsam.algo

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
        if(!this.tienenInformacionCargadaEnLosStrings() || (this.fechaDeAlta > LocalDate.now()) || (this.diasParaViajar < 0) || (!this.tieneDestinoSoñado())){
            throw CustomException("Hay informacion vacia")
        }
    }

    fun cambiarCriterio(unCriterio: Criterio) {
        criterio = unCriterio
    }

    fun tienenInformacionCargadaEnLosStrings() =
        !(this.nombre.isNullOrEmpty() && this.apellido.isNullOrEmpty() && this.username.isNullOrEmpty() && this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(unUsuario: Usuario) = amigos.add(unUsuario)

    fun consultarPuntaje(unItinerario: Itinerario) = unItinerario.verPuntaje(this)


    fun puedoPuntuar(unItinerario: Itinerario) = !((unItinerario.sosMiCreador(this)) || (this.yaPuntee(unItinerario))) && this.conoceDestino(unItinerario.destino)

    fun yaPuntee(unItinerario: Itinerario) = unItinerario.puntajes.containsKey(this.username)

    fun puntuar(unItinerario: Itinerario, puntaje: Int){
        if((puntaje<1) || (puntaje>10)){
            throw CustomException("El puntaje tiene que ser del 1 al 10")
        }
        else if(!puedoPuntuar(unItinerario)){
                throw CustomException("No puedo puntuar")
        }
        else {
            unItinerario.darPuntaje(this, puntaje)
        }
    }

    fun conoceDestino(unDestino: Destino) =
        (destinosDeseados.contains(unDestino) || destinosVisitados.contains(unDestino))

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