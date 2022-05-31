package ar.edu.unsam.algo

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class Usuario(
    val nombre: String,
    val apellido: String,
    var username: String,
    var fechaDeAlta: LocalDate,
    val paisDeResidencia: String,
    var diasParaViajar: Int,
    val amigos: MutableList<Usuario> = mutableListOf(),
    val destinosDeseados: MutableList<Destino> = mutableListOf(),
    val destinosVisitados: MutableList<Destino> = mutableListOf(),
) : Datos {
    override var id: Int = 0
    lateinit var criterioParaItinerario: CriterioItinerario
    lateinit var criterioParaVehiculo: CriterioVehiculo
    var presupuesto: Double = 0.0
    var direccionDeCorreo: String = ""

    val itinerariosUsuario: MutableList<Itinerario> = mutableListOf()
    val listaViajes: MutableList<Viaje> = mutableListOf()
    val accionesActivas: MutableList<Acciones> = mutableListOf()

    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }


    fun direccionDeCoreo() = direccionDeCorreo

    fun puntuarItinerarios(puntaje: Int) {
        if (itinerariosUsuario.isEmpty()) throw BusinessException("No tiene itinerarios para puntuar")
        else itinerariosUsuario.forEach { this.puntuar(it, puntaje) }
    }


    fun obtenerAmigoConMenosDestinos() = this.amigos.minByOrNull { it.destinosVisitados.size } //amigos.first()

    fun obtener(itinerario: Itinerario) = itinerariosUsuario.add(itinerario)

    fun obtenerItinerarios(amigo: Usuario) =
        this.itinerariosUsuario.addAll(amigo.itinerariosUsuario.filter { it.sosMiCreador(amigo) })

    fun transferirItinerariosAAmigoConMenosDestinos() {
        this.validarQueTieneAmigo()
        this.obtenerAmigoConMenosDestinos()?.obtenerItinerarios(this)
    }

    fun validarQueTieneAmigo() {
        if (amigos.isEmpty()) throw BusinessException("No tiene amigos")
    }

    override fun coincidencia(cadena: String): Boolean =
        coicidenciaParcialNombreApellido(cadena) || coincidenciaTotalUsername(cadena)

    fun coincidenciaTotalUsername(cadena: String) = username.equals(cadena, ignoreCase = false)

    fun coicidenciaParcialNombreApellido(cadena: String) =
        nombre.contains(cadena, ignoreCase = true) || apellido.contains(cadena, ignoreCase = true)

    fun tieneDestinoSoñado() = destinosDeseados.isNotEmpty()

    fun hacerseAmigoDeLosQueConocen(listaDeUsuarios: MutableList<Usuario>, destino: Destino) {
        amigos.addAll(this.amigosQueConocenA(listaDeUsuarios, destino))
    }

    fun amigosQueConocenA(listaDeUsuarios: MutableList<Usuario>, destino: Destino) =
        listaDeUsuarios.filter { it.conoceDestino(destino) }

    fun agregarDestinomasCarodeMisAmigos() = amigos.forEach { it.agregarDestinoMasCaroA(this) }

    fun agregarDestinoMasCaroA(amigo: Usuario) = amigo.destinosDeseados.add(this.destinoMasCaro())

    fun destinoMasCaro(): Destino {
        this.ordenarDestinosPorPrecio()
        if (!this.destinosDeseados.isEmpty()) return this.destinosDeseados.last() else throw BusinessException("No tiene destino deseado")
    }

    fun ordenarDestinosPorPrecio() {
        if (destinosDeseados.isEmpty()) destinosDeseados.sortBy { it.precio(this) }
    }

    override fun validacion() {
        if (!esValido()) {
            throw FaltaCargarInformacionException(
                "Hay informacion vacia, Nombre: $nombre, apellido: $apellido, username: $username, pais de residencia: $paisDeResidencia\ndias para viajar: $diasParaViajar, destinos deseados: $destinosDeseados"
            )
        }
    }

    override fun esValido(): Boolean =
        this.tieneInformacionCargadaEnStrings() && (!tieneFechaAltaInvalida()) && (!tieneDiasParaViajarInvalidos()) && (this.tieneDestinoSoñado())

    fun tieneDiasParaViajarInvalidos(): Boolean = this.diasParaViajar < 0

    fun tieneFechaAltaInvalida(): Boolean = this.fechaDeAlta > LocalDate.now()

    fun cambiarCriterio(criterio: CriterioItinerario) {
        this.criterioParaItinerario = criterio
    }

    fun cambiarCriterioVehiculoA(criterio: CriterioVehiculo) {
        this.criterioParaVehiculo = criterio
    }

    fun tieneInformacionCargadaEnStrings() =
        !(this.nombre.isNullOrEmpty() && this.apellido.isNullOrEmpty() && this.username.isNullOrEmpty() && this.paisDeResidencia.isNullOrEmpty())

    fun agregarAmigo(usuario: Usuario) = amigos.add(usuario)

    fun consultarPuntaje(itinerario: Itinerario) = itinerario.verPuntaje(this)

    fun puedoPuntuar(itinerario: Itinerario) =
        !(esCreadorDe(itinerario)) && !itinerario.yaPuntuo(this.username) && this.conoceDestino(itinerario.destino)

    fun puntuar(itinerario: Itinerario, puntaje: Int) {
        if ((puntaje < 1) || (puntaje > 10) || !puedoPuntuar(itinerario)) {
            throw BusinessException(
                "No puede puntuar el itinerario, usted es el creador o ya puntuo el itinerario o no conce el destino\n" +
                        "Revise que el puntaje ingresado sea mayor a 1 y menor que 10 puntaje: $puntaje"
            )
        } else {
            itinerario.recibirPuntaje(this, puntaje)
        }
    }

    fun contieneElItinerario(itinerario: Itinerario) = this.itinerariosUsuario.contains(itinerario)

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
        this.diasSuficientes(itinerario) and criterioParaItinerario.acepta(itinerario)

    fun diasSuficientes(itinerario: Itinerario) = diasParaViajar >= itinerario.cantidadDeDias()

    fun amigoConoceDestino(destino: Destino) = amigos.any { it.conoceDestino(destino) }

    fun puedoEditar(itinerario: Itinerario) = esCreadorDe(itinerario) || this.soyAmigoEditor(itinerario)

    fun soyAmigoEditor(itinerario: Itinerario) =
        (this.soyAmigoDelCreador(itinerario.creador) && this.conoceDestino(itinerario.destino))

    fun soyAmigoDelCreador(otroUsuario: Usuario) = amigos.contains(otroUsuario)

    fun leGustaVehiculo(vehiculo: Vehiculo) = criterioParaVehiculo.aceptaVehiculo(vehiculo)

    fun puedeRealizar(viaje: Viaje) = presupuesto >= viaje.costoTotal(this)

    fun validarViaje(viaje: Viaje) {
        if (!puedeRealizar(viaje)) {
            throw BusinessException("No puede viajar porque no tenes el presupuesto suficiente")
        }
    }

    fun estaEnLista(viaje: Viaje) = listaViajes.contains(viaje)

    fun realizar(viaje: Viaje) {
        validarViaje(viaje)
        destinosVisitados.add(viaje.getDestino())
        ejecutarAcciones(viaje)

    }

    fun ejecutarAcciones(viaje: Viaje) {

        if (accionesActivas.isNotEmpty())
            accionesActivas.forEach { it.ejecutar(this, viaje) }
    }


    fun amigosQueDeseanViaje(viaje: Viaje) = amigos.filter { it.deseoDestino(viaje.getDestino()) }

    fun activarAccion(accion: Acciones) = accionesActivas.add(accion)

    fun activarAcciones(listaDeAcciones: MutableList<Acciones>) = accionesActivas.addAll(listaDeAcciones)

    fun desactivarAccion(accion: Acciones) = accionesActivas.remove(accion)


    fun deseoDestino(destino: Destino) = destinosDeseados.contains(destino)


}

