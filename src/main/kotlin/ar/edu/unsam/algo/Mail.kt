package ar.edu.unsam.algo

class Mail {
    fun armarMail(receptor: Usuario,emisor:Usuario) = "Hola! ${receptor.nombre},  ${emisor.nombre} ${emisor.apellido} visito "
}