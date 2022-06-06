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
    var direccionDeCorreo: String = ""

    val observerDeViajesActivas: MutableSet<ObserverDeViajes> = mutableSetOf()

    companion object {
        var ANTIGUEDAD_MAXIMA = 15
    }

    fun obtenerAmigoConMenosDestinos() = this.amigos.minByOrNull { it.destinosVisitados.size } //amigos.first()

    override fun coincidencia(cadena: String): Boolean =
        coicidenciaParcialNombreApellido(cadena) || coincidenciaTotalUsername(cadena)

    fun coincidenciaTotalUsername(cadena: String) = username.equals(cadena, ignoreCase = false)

    fun coicidenciaParcialNombreApellido(cadena: String) =
        nombre.contains(cadena, ignoreCase = true) || apellido.contains(cadena, ignoreCase = true)

    fun tieneDestinoSoniado() = destinosDeseados.isNotEmpty()

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
        this.tieneInformacionCargadaEnStrings() && (!tieneFechaAltaInvalida()) && (!tieneDiasParaViajarInvalidos()) && (this.tieneDestinoSoniado())

    fun tieneDiasParaViajarInvalidos(): Boolean = this.diasParaViajar < 0

    fun tieneFechaAltaInvalida(): Boolean = this.fechaDeAlta > LocalDate.now()

    fun cambiarCriterio(criterio: CriterioItinerario) {
        this.criterioParaItinerario = criterio
    }

    fun cambiarCriterioVehiculoA(criterio: CriterioVehiculo) {
        this.criterioParaVehiculo = criterio
    }

    fun tieneInformacionCargadaEnStrings() =
        !(this.nombre.isEmpty() && this.apellido.isEmpty() && this.username.isEmpty() && this.paisDeResidencia.isEmpty())

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

    fun realizar(viaje: Viaje) {
        destinosVisitados.add(viaje.getDestino())
        accionDe(viaje)

    }

    fun accionDe(viaje: Viaje) {
        observerDeViajesActivas.forEach { it.realizaViaje(this, viaje) }
    }

    fun amigosQueConocenDestino(destino: Destino) = amigos.filter { it.deseoDestino(destino) }

    fun activarAccion(accion: ObserverDeViajes) = observerDeViajesActivas.add(accion)

    fun activarAcciones(listaDeAcciones: MutableList<ObserverDeViajes>) = observerDeViajesActivas.addAll(listaDeAcciones)

    fun desactivarAccion(accion: ObserverDeViajes) = observerDeViajesActivas.remove(accion)

    fun deseoDestino(destino: Destino) = destinosDeseados.contains(destino)

    fun realizarTarea(tarea: Tarea, mailSender: MailSender){
        tarea.realizarYNotificarTarea(this, mailSender)
    }

    fun realizarVariasTareas(listaDeTarea: MutableList<Tarea>, mailSender: MailSender){
        listaDeTarea.forEach { this.realizarTarea(it, mailSender) }
    }

}

