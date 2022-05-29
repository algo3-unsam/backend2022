package ar.edu.unsam.algo

class ListaCorreo{
    val registrados : MutableList<Usuario> = mutableListOf()
    var validacionEnvio : ValidacionEnvio = EnvioRestringido()
    val postObservers = mutableListOf<PostObserver>()

    fun recibirPost(post: Post) {
        validacionEnvio.validarPost(post, this)
        postObservers.forEach { it.postEnviado(post, this) }
    }

    fun getMailsDestino(post: Post) = this.registrados
        .filter { usuario -> usuario != post.emisor}
        .map { it.email }
        .joinToString(", ")

    fun getMailsDestinoDeseado(viaje: Viaje, post: Post) = post.emisor.deseanDestinoAmigos(viaje)
        .map { it.email }
        .joinToString(", ")


    fun agregarPostObserver(postObserver: PostObserver) {
        this.postObservers.add(postObserver)
    }

    /*********************** Definiciones internas  ***************************/
    fun agregarUsuario(usuario: Usuario) {
        registrados.add(usuario)
    }
    fun contieneUsuario(emisor: Usuario): Boolean = registrados.contains(emisor)
}

interface ValidacionEnvio {
    fun validarPost(post: Post, listaCorreo: ListaCorreo)
}

class EnvioRestringido : ValidacionEnvio {
    override fun validarPost(post: Post, listaCorreo: ListaCorreo) {
        if (!listaCorreo.contieneUsuario(post.emisor)) {
            throw BusinessException("No puede enviar un mensaje porque no pertenece a la lista")
        }
    }
}