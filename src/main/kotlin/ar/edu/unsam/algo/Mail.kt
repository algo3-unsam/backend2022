package ar.edu.unsam.algo

class Mail {
    fun armarMail(receptor: Usuario,emisor:Usuario,viaje: Viaje) = "Hola! ${receptor.nombre},  ${emisor.nombre} ${emisor.apellido} visito ${viaje.itinerario.destino.pais} ${viaje.itinerario.destino.ciudad}"

}
