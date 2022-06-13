package ar.edu.unsam.algo

interface ObserverDeViajes {
    fun realizaViaje(usuario: Usuario, viaje: Viaje)
}

class RealizaViajeLocal : ObserverDeViajes {
    override fun realizaViaje(usuario: Usuario, viaje: Viaje) {
        if(!viaje.esLocal()) usuario.cambiarCriterio(Localista)
    }

}

class MandarMailAAmigosQueDeseanDestino : ObserverDeViajes {
    lateinit var mailSender: MailSender
    override fun realizaViaje(usuario: Usuario, viaje: Viaje) {
        usuario.amigosQueDeseanDestino(viaje.getDestino()).forEach { armarMail(usuario, viaje, it) }
    }

    fun armarMail(emisor: Usuario, viaje: Viaje, receptor: Usuario) {
        mailSender.sendMail(
            Mail(
                from = direccionCorreoApp,
                to = receptor.direccionDeCorreo,
                subject = "Visitaron un destino que te puede interesar",
                content = getBody(emisor, viaje, receptor)
            )
        )

    }

    fun getBody(emisor: Usuario, viaje: Viaje, receptor: Usuario) = "Hola! ${receptor.nombre}, ${emisor.nombre} ${emisor.apellido} visito ${viaje.getDestino().pais} ${viaje.getDestino().ciudad}"

}


class AgregarAListaDeItinerariosParaPuntuar() : ObserverDeViajes {
    override fun realizaViaje(usuario: Usuario, viaje: Viaje) {
        usuario.agregarItinerarioAPuntuar(viaje.itinerario)
    }

}

class RealizaViajeConConvenio : ObserverDeViajes {
    override fun realizaViaje(usuario: Usuario, viaje: Viaje) {
        if(!viaje.esLocal()){
            cambiarCriterio(usuario,viaje)
        }
    }

    fun cambiarCriterio(usuario: Usuario, viaje: Viaje) {
        usuario.cambiarCriterioVehiculoA(Selectivo(Vehiculo.primeraMarcaConConvenio()))
    }

}