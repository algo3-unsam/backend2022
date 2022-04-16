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

    lateinit var criterio: CriterioItinerario

    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }

    fun tieneDestinoSoñado() = destinosDeseados.isNotEmpty()

    fun esValido() {
        if (!this.tienenInformacionCargadaEnLosStrings() || (tieneFechaAltaValida()) || (tieneDiasParaViajarValidos()) || (!this.tieneDestinoSoñado())) {
            throw FaltaCargarInformacionException(
                "Hay informacion vacia, Nombre: $nombre, apellido: $apellido, username: $username, pais de residencia: $paisDeResidencia\n" + "dias para viajar: $diasParaViajar, destinos deseados: $destinosDeseados"
            )
        }
    }

    fun tieneDiasParaViajarValidos(): Boolean = this.diasParaViajar < 0

    fun tieneFechaAltaValida(): Boolean = this.fechaDeAlta > LocalDate.now()

    fun cambiarCriterio(criterio: CriterioItinerario) {
        this.criterio = criterio
    }

    fun tienenInformacionCargadaEnLosStrings() =
        !(this.nombre.isNullOrEmpty() && this.apellido.isNullOrEmpty() && this.username.isNullOrEmpty() && this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(usuario: Usuario) = amigos.add(usuario)

    fun consultarPuntaje(itinerario: Itinerario) = itinerario.verPuntaje(this)

    fun puedoPuntuar(itinerario: Itinerario) =
            !(esCreadorDe(itinerario)) && !itinerario.yaPuntuo(this.username) && this.conoceDestino(itinerario.destino)

    fun puntuar(itinerario: Itinerario, puntaje: Int) {
        if ((puntaje < 1) || (puntaje > 10) || !puedoPuntuar(itinerario)) {
            throw BusinessException("No puede puntuar el itinerario, usted es el creador o ya puntuo el itinerario o no conce el destino\n" +
                    "Revise que el puntaje ingresado sea mayor a 1 y menor que 10 puntaje: $puntaje")
        }else {
            itinerario.recibirPuntaje(this, puntaje)
        }
    }

    fun esCreadorDe(itinerario: Itinerario) = itinerario.creador === this

    fun conoceDestino(destino: Destino) =
        (this.estaEnDeseados(destino) || destinosVisitados.contains(destino))

    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())

    fun descuentoPorAntiguedad() = if (antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(destino: Destino) = this.paisDeResidencia.equals(destino.pais, ignoreCase = true)

    fun estaEnDeseados(destino: Destino) = destinosDeseados.contains(destino)

    fun deseadoMasCaro() = destinosDeseados.maxOf { it.precio(this) }

    fun destinoMasCaroQueDeseadoMasCaro(itinerario: Itinerario) =
        itinerario.destino.precio(this) > this.deseadoMasCaro()

    fun puedeRealizarItinerario(itinerario: Itinerario) =
        this.diasSuficientes(itinerario) and criterio.acepta(itinerario)

    fun diasSuficientes(itinerario: Itinerario) = diasParaViajar >= itinerario.cantDias

    fun amigoConoceDestino(destino: Destino) = amigos.any { it.conoceDestino(destino) }

    fun puedoEditar(itinerario: Itinerario) = esCreadorDe(itinerario) || this.soyAmigoEditor(itinerario)

    fun soyAmigoEditor(itinerario: Itinerario) =
        (this.soyAmigoDelCreador(itinerario.creador) && this.conoceDestino(itinerario.destino))

    fun soyAmigoDelCreador(otroUsuario: Usuario) = amigos.contains(otroUsuario)

    fun leGustaLosVehiculosAntiguos(vehiculo: Vehiculo) = vehiculo.antiguedad() < 2

    fun soySupersticioso(vehiculo: Vehiculo) = vehiculo.anioDeFabricacion.year % 2 == 0

}