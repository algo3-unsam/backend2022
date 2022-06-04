package ar.edu.unsam.algo

abstract class Tarea(var nombre: String) {
    abstract fun realizarTarea(usuario: Usuario)

    fun notificarTarea(usuario: Usuario){
        ServiceLocator.mailSender.sendMail(
            Mail(
                from = "app@gmail.com",
                to = usuario.direccionDeCorreo,
                subject = "Tarea Realizada",
                content = "Se realiza la tarea ${this.nombre}"
            ))
    }

    fun realizarYNotificarTarea(usuario: Usuario){
        realizarTarea(usuario)
        notificarTarea(usuario)
    }

}

class PuntuarItinerarios(val repoDeItinerarios: RepositorioDeItinerarios, var puntaje: Int) :Tarea("PuntuarTodosLosItinerarios"){
    override fun realizarTarea(usuario: Usuario){
        repoDeItinerarios.filtrarPorPuntuables(usuario).forEach { usuario.puntuar(it,puntaje) }
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