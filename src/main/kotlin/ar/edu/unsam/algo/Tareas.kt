package ar.edu.unsam.algo

abstract class Tarea(var nombre: String, var mailSender: MailSender) {

    abstract fun realizarTarea(usuario: Usuario)
    fun notificarTarea(usuario: Usuario){
        mailSender.sendMail(
            Mail(
                from = direccionCorreoApp,
                to = usuario.direccionDeCorreo,
                subject = Mensaje(),
                content = Mensaje()
            ))
    }

    fun Mensaje() = "Se realiza la tarea ${this.nombre}"

    fun realizarYNotificarTarea(usuario: Usuario){
        realizarTarea(usuario)
        notificarTarea(usuario)
    }

}

class PuntuarItinerarios(var puntaje: Int, mailSender: MailSender) :Tarea("PuntuarTodosLosItinerarios", mailSender){
    override fun realizarTarea(usuario: Usuario){
        usuario.listaItinerariosParaPuntuar.forEach{usuario.puntuar(it,puntaje)}
    }
}

class TranseferirItinerarios(val repoDeItinerarios: RepositorioDeItinerarios,
                             mailSender: MailSender
): Tarea("Transferir Itinerarios", mailSender){
    override fun realizarTarea(usuario: Usuario) {
        usuario.obtenerAmigoConMenosDestinos()?.let { repoDeItinerarios.cambiarCreador(usuario, it) }
    }
}

class AgregarAmigos(val repositorioDeUsuarios: RepositorioDeUsuarios, val destino: Destino,
                    mailSender: MailSender
) : Tarea("Agregar amigos con destino conocido", mailSender){
    override fun realizarTarea(usuario: Usuario) {
        val usuariosAAgregarAAmigos = repositorioDeUsuarios.usuariosQueConocenUnDestinoYNoSonAmigosDeOtroUsuario(destino,usuario)
        usuariosAAgregarAAmigos.forEach {usuario.agregarAmigo(it)  }
    }
}

class AgregarDeseadoCaroDeAmigos(mailSender: MailSender) : Tarea("Agregar el deseado mas caro de tus amigos",
    mailSender
){
    override fun realizarTarea(usuario: Usuario) {
        usuario.agregarDestinomasCarodeMisAmigos()
    }
}