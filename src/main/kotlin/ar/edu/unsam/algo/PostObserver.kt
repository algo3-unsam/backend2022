package ar.edu.unsam.algo

interface PostObserver {
    fun postEnviado(post: Post, listaCorreo: ListaCorreo)
}

class MailObserver(var viaje: Viaje) : PostObserver{
    override fun postEnviado(post: Post, listaCorreo: ListaCorreo) {
        ServiceLocator.mailSender.sendMail(
            Mail(from = post.mailEmisor(),
                to = listaCorreo.getMailsDestinoDeseado(viaje, post),
                subject = post.mailSubject(),
                content = post.mensaje)
        )
    }

}