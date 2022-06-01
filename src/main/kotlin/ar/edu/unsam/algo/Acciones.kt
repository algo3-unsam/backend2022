package ar.edu.unsam.algo

interface Acciones {
    fun ejecutar(usuario: Usuario, viaje: Viaje)
}

class RealizaViajeLocal() : Acciones {
    override fun ejecutar(usuario: Usuario, viaje: Viaje) {
        if (!viaje.esLocal()) usuario.cambiarCriterio(Localista)
    }

}

class MandarMailAAmigosQueDeseanDestino() : Acciones {
    val direccionDeCorreo: String = "app@holamundo.com"
    lateinit var mailSender: MailSender
    override fun ejecutar(usuario: Usuario, viaje: Viaje) {
        usuario.amigosQueConocenDestino(viaje.getDestino()).forEach { armarMail(usuario, viaje, it) }
    }

    fun armarMail(emisor: Usuario, viaje: Viaje, receptor: Usuario) {
        mailSender.sendMail(
            Mail(
                from = direccionDeCorreo,
                to = receptor.direccionDeCorreo,
                subject = getSubject(),
                content = getBody(emisor, viaje, receptor)
            )
        )

    }


    fun getSubject() = "Visitaron un destino que te puede interesar"

    fun getBody(emisor: Usuario, viaje: Viaje, receptor: Usuario) = "Hola! ${receptor.nombre}, ${emisor.nombre} visito ${viaje.getDestino().pais} ${viaje.getDestino().ciudad}"

}


class AgregarAListaDeItinerariosParaPuntuar() : Acciones {
    override fun ejecutar(usuario: Usuario, viaje: Viaje) {
        if (usuario.estaParaPuntuar(viaje.itinerario)) throw BusinessException("El itinirario indicado ya esta para puntuar") else usuario.obtener(
            viaje.itinerario
        )
    }
}

class RealizaViajeConConvenio() : Acciones {
    override fun ejecutar(usuario: Usuario, viaje: Viaje) {
        if (viaje.vehiculoConConvenio()) throw BusinessException("El viaje ya tiene un vehiculo con convenio") else cambiarCriterio(
            usuario,
            viaje
        )
    }

    fun cambiarCriterio(usuario: Usuario, viaje: Viaje) {
        usuario.cambiarCriterioVehiculoA(Selectivo(viaje.vehiculo.marcasConConvenio().first()))
    }

}