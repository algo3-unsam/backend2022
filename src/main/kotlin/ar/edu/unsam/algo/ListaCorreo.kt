package ar.edu.unsam.algo

class ListaCorreo{
    val usuariosRegistrados : MutableList<Usuario> = mutableListOf()
    val direccionDeCorreo : String = "app@holamundo.com"
    val subject: String =  "Visitaron un destino que te puede interesar"
    var mailsEnviados = 0
    fun registrar(usuario: Usuario){
        usuariosRegistrados.add(usuario)
    }

    fun sendMail(viaje: Viaje, emisor: Usuario){
        emisor.amigosQueDeseanViaje(viaje).forEach { armarMail(viaje, emisor, it) }
    }
    fun armarMail(viaje: Viaje, emisor: Usuario, receptor: Usuario){
        mailsEnviados()
        serviceLocator.mailSender.sendMail(
        Mail(
            from = direccionDeCorreo,
            to = receptor.direccionDeCorreo,
            subject = subject,
            content = getBody(viaje, emisor, receptor)))
    }

    fun mailsEnviados() {
        mailsEnviados += 1
    }

    fun getBody(viaje: Viaje, emisor: Usuario, receptor: Usuario) = "Hola! ${receptor.nombre}, ${emisor.nombre} visito ${viaje.getDestino().pais} ${viaje.getDestino().ciudad}"

}