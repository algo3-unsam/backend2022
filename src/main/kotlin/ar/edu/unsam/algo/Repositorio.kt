package ar.edu.unsam.algo

class Repositorio<Elemento : Datos> {
    val elementos: MutableList<Elemento> = mutableListOf<Elemento>()
    var idSiguiente = 1

    fun cantElementos() = elementos.size

    fun create(elemento: Elemento){
        creacionCorrecta(elemento)
        yaEstaCreado(elemento)
        agregarAlRepo(elemento)
    }

    fun creacionCorrecta(elemento: Elemento){
       elemento.validacion()
    }

    fun yaEstaCreado(elemento: Elemento){
        if(estaEnRepo(elemento)){
            throw BusinessException("YA ESTA EN EL REPO")
        }
    }
    fun agregarAlRepo(elemento: Elemento){
        elementos.add(elemento)
        asignarId(elemento)
    }

    fun asignarId(elemento: Elemento){
        elemento.id  = idSiguiente
        idSiguiente++
    }

    fun estaEnRepo(elemento: Elemento) = elementos.any{ it.id == elemento.id}

    fun delete(elemento: Elemento) {
        excepcionPorNoExistenciaEnRepo(elemento)
        elementos.remove(elemento)
    }

    fun update(elementoModificado: Elemento){
        excepcionPorNoExistenciaEnRepo(elementoModificado)
        creacionCorrecta(elementoModificado)
        var elementoABorrar = getById(elementoModificado.id)
        delete(elementoABorrar)
        agregaElementoModificado(elementoModificado)
    }

    fun agregaElementoModificado(elementoModificado: Elemento) {
        elementos.add(elementoModificado)
    }

    fun excepcionPorNoExistenciaEnRepo(elemento: Elemento){
        if(!estaEnRepo(elemento)){
            throw BusinessException("NO ESTA EN EL REPO")
        }
    }

    fun getById(idABuscar: Int):Elemento{
        return elementos.first { it.id == idABuscar  }
    }

    fun search(cadena: String) = elementos.filter { it.coincidencia(cadena) }

    fun crearOModificar(elemento: Elemento){
        if(estaEnRepo(elemento)){
            this.update(elemento)
        }
        else{
            this.create(elemento)
        }
    }
}