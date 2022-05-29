package ar.edu.unsam.algo

data class Post(val emisor: Usuario, val receptor: Usuario ,val asunto: String, val mensaje: String, val viaje: Viaje){
    fun mailEmisor() = "app@holamundo.com"
    fun mailSubject() = "Visitaron un destino que te puede interesar"
    fun mailBody() =  "Hola! ${emisor.nombre}, ${receptor.nombre} ${receptor.apellido} visitó ${viaje.itinerario.destino.pais} ${viaje.itinerario.destino.ciudad} "
}