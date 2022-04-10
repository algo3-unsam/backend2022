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

    fun esValido() {
        if (!this.tienenInformacionCargadaEnLosStrings() || (this.fechaDeAlta > LocalDate.now()) || (this.diasParaViajar < 0) || (!this.tieneDestinoSoñado())) {
            throw FaltaCargarInformacionException(
                "Hay informacion vacia, Nombre: $nombre, apellido: $apellido, username: $username, pais de residencia: $paisDeResidencia\n" + "dias para viajar: $diasParaViajar, destinos deseados: $destinosDeseados"
            )
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

    fun puedoPuntuar(unItinerario: Itinerario) =
            !(esCreadorDe(unItinerario)) && !unItinerario.yaPuntuo(this.username) && this.conoceDestino(unItinerario.destino)

    fun puntuar(unItinerario: Itinerario, puntaje: Int) {
        if ((puntaje < 1) || (puntaje > 10) || !puedoPuntuar(unItinerario)) {
            throw BusinessException("No puede puntuar el itinerario, usted es el creador o ya puntuo el itinerario o no conce el destino\n" +
                    "Revise que el puntaje ingresado sea mayor a 1 y menor que 10 puntaje: $puntaje")
        }else {
            unItinerario.recibirPuntaje(this, puntaje)
        }
    }

    fun esCreadorDe(Itinerario: Itinerario) = Itinerario.creador === this

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

    fun amigoConoceDestino(unDestino: Destino) = amigos.any { it.conoceDestino(unDestino) }

    fun puedoEditar(unItinerario: Itinerario) = esCreadorDe(unItinerario) || this.soyAmigoEditor(unItinerario)

    fun soyAmigoEditor(unItinerario: Itinerario) =
        (this.soyAmigoDelCreador(unItinerario.creador) && this.conoceDestino(unItinerario.destino))

    fun soyAmigoDelCreador(otroUsuario: Usuario) = amigos.contains(otroUsuario)

}