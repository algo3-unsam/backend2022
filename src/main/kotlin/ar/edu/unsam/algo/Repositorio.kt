package ar.edu.unsam.algo

class Repositorio<Elemento : Datos> {
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
        excepcionPorNoExistenciaEnRepo(elementoModificado.id)
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