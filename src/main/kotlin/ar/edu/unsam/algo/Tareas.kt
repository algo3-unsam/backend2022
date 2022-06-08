package ar.edu.unsam.algo

abstract class Tarea(var nombre: String) {
    abstract fun realizarTarea(usuario: Usuario)
    fun notificarTarea(usuario: Usuario, mailSender: MailSender){
        mailSender.sendMail(
            Mail(
                from = direccionCorreoApp,
                to = usuario.direccionDeCorreo,
                subject = Mensaje(),
                content = Mensaje()
            ))
    }

    fun Mensaje() = "Se realiza la tarea ${this.nombre}"

    fun realizarYNotificarTarea(usuario: Usuario, mailSender: MailSender){
        realizarTarea(usuario)
        notificarTarea(usuario, mailSender)
    }

}

class PuntuarItinerarios(var puntaje: Int) :Tarea("PuntuarTodosLosItinerarios"){
    override fun realizarTarea(usuario: Usuario){
        usuario.listaItinerariosParaPuntuar.forEach{usuario.puntuar(it,puntaje)}
    }
}

class TranseferirItinerarios(val repoDeItinerarios: RepositorioDeItinerarios): Tarea("Transferir Itinerarios"){
    override fun realizarTarea(usuario: Usuario) {
        usuario.obtenerAmigoConMenosDestinos()?.let { repoDeItinerarios.cambiarCreador(usuario, it) }
    }
}

class AgregarAmigos(val repositorioDeUsuarios: RepositorioDeUsuarios, val destino: Destino) : Tarea("Agregar amigos con destino conocido"){
    override fun realizarTarea(usuario: Usuario) {
        repositorioDeUsuarios.agregarAmigosConDestinoConocido(usuario,destino)
    }
}

class AgregarDeseadoCaroDeAmigos : Tarea("Agregar el deseado mas caro de tus amigos"){
    override fun realizarTarea(usuario: Usuario) {
        usuario.agregarDestinomasCarodeMisAmigos()
    }
}