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

    fun tieneDestinoSoñado() = destinosDeseados.isNotEmpty()

    fun esValido(){
        if(!this.tienenInformacionCargadaEnLosStrings()  || (this.tieneDiasParaViajarValidos()) || (!this.tieneDestinoSoñado())){
            throw FaltaCargarInformacion("Hay informacion vacia")
        }
        if( (this.tieneFechaAltaValida())){
            throw FechaInvalida("La fecha es invalida por ser posterior a la fecha del dia de hoy")
        }
    }

     fun tieneDiasParaViajarValidos(): Boolean = this.diasParaViajar < 0

    fun tieneFechaAltaValida(): Boolean = this.fechaDeAlta > LocalDate.now()

    fun cambiarCriterio(unCriterio: Criterio) {
        criterio = unCriterio
    }

    fun tienenInformacionCargadaEnLosStrings() =
        !(this.nombre.isNullOrEmpty() && this.apellido.isNullOrEmpty() && this.username.isNullOrEmpty() && this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(unUsuario: Usuario) = amigos.add(unUsuario)

    fun consultarPuntaje(unItinerario: Itinerario) = unItinerario.verPuntaje(this)

    fun puedoPuntuar(unItinerario: Itinerario) = !(unItinerario.sosMiCreador(this)) && !unItinerario.yaPuntuo(this.username) && this.conoceDestino(unItinerario.destino)

    fun puntuar(unItinerario: Itinerario, puntaje: Int){
        if((puntaje<1) || (puntaje>10)){
            throw FaltaCargarInformacion("El puntaje tiene que ser del 1 al 10")
        }
        else if(!puedoPuntuar(unItinerario)){
                throw FaltaCargarInformacion("No puedo puntuar")
        }
        else {
            unItinerario.recibirPuntaje(this, puntaje)
        }
    }

    fun conoceDestino(unDestino: Destino) =
        (this.estaEnDeseados(unDestino) || destinosVisitados.contains(unDestino))

    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())

    fun descuentoPorAntiguedad() = if (antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(unDestino: Destino) = this.paisDeResidencia.equals(unDestino.pais, ignoreCase = true)

    fun estaEnDeseados(destino: Destino) = destinosDeseados.contains(destino)

    fun deseadoMasCaro() = destinosDeseados.maxOf { it.precio(this) }

    fun destinoMasCaroQueDeseadoMasCaro(unItinerario: Itinerario) =
        unItinerario.destino.precio(this) > this.deseadoMasCaro()

    fun puedeRealizarItinerario(unItinerario: Itinerario) =
        this.diasSuficientes(unItinerario) and criterio.acepta(unItinerario)

    fun diasSuficientes(unItinerario: Itinerario) = diasParaViajar >= unItinerario.cantDias

    fun amigoConoceDestino(unDestino: Destino) = amigos.any{it.conoceDestino(unDestino)}

    fun puedoEditar(unItinerario: Itinerario) = unItinerario.sosMiCreador(this) || this.soyAmigoEditor(unItinerario)

    fun soyAmigoEditor(unItinerario: Itinerario) = (this.soyAmigoDelCreador(unItinerario.creador) && this.conoceDestino(unItinerario.destino))

   fun soyAmigoDelCreador(otroUsuario: Usuario) = amigos.contains(otroUsuario)

}