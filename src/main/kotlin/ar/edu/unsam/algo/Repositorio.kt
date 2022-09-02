package ar.edu.unsam.algo

open class Repositorio<Elemento : Datos> {
    val elementos: MutableList<Elemento> = mutableListOf<Elemento>()
    var idSiguiente = 1

    fun cantElementos() = elementos.size

    fun create(elemento: Elemento){
        elemento.validacion()
        yaEstaCreado(elemento)
        agregarAlRepo(elemento)
    }

    fun yaEstaCreado(elemento: Elemento){
        if(estaEnRepo(elemento.id)){
            throw BusinessException("YA ESTA EN EL REPO")
        }
    }

    fun agregarAlRepo(elemento: Elemento){
        elementos.add(elemento)
        asignarId(elemento)
    }

    fun asignarId(elemento: Elemento){
        elemento.id  = idSiguiente++
    }

    fun estaEnRepo(idABuscar: Int) = elementos.any{ it.id == idABuscar}

    fun delete(elemento: Elemento) {
        excepcionPorNoExistenciaEnRepo(elemento.id)
        elementos.remove(elemento)
    }

    fun update(elementoModificado: Elemento){
        elementoModificado.validacion()
        var elementoABorrar = getById(elementoModificado.id)
        delete(elementoABorrar)
        agregaElementoModificado(elementoModificado)
    }

    fun agregaElementoModificado(elementoModificado: Elemento) {
        elementos.add(elementoModificado)
    }

    fun excepcionPorNoExistenciaEnRepo(idABuscar: Int){
        if(!estaEnRepo(idABuscar)){
            throw BusinessException("NO ESTA EN EL REPO")
        }
    }

    fun getById(idABuscar: Int):Elemento{
        excepcionPorNoExistenciaEnRepo(idABuscar)
        return elementos.first { it.id == idABuscar  }
    }

    fun search(cadena: String) = elementos.filter { it.coincidencia(cadena) }

    fun crearOModificar(elemento: Elemento){
        if(estaEnRepo(elemento.id)){
            this.update(elemento)
        }
        else{
            this.create(elemento)
        }
    }
}

class RepositorioDeItinerarios : Repositorio<Itinerario>() {

    fun filtrarPorItinerariosCreados(unUsuario: Usuario): List<Itinerario>{
        return elementos.filter { it.sosMiCreador(unUsuario) }
    }

    fun cambiarCreador(viejoCreador: Usuario, nuevoCreador: Usuario){
        filtrarPorItinerariosCreados(viejoCreador).forEach { it.cambiarCreador(nuevoCreador) }
    }

    fun filtrarPorPuntuables(unUsuario: Usuario): List<Itinerario> {
        return elementos.filter { unUsuario.puedoPuntuar(it) }
    }

}

class RepositorioDeUsuarios : Repositorio<Usuario>(){
    fun usuariosQueConocenUnDestinoYNoSonAmigosDeOtroUsuario(unDestino: Destino, usuario: Usuario): List<Usuario>{
        return elementos.filter {it.conoceDestino(unDestino) && usuario.puedoAgregarAAmigos(it)}
    }

}