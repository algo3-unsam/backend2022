package ar.edu.unsam.algo

class Repositorio<Elemento : Datos> {
    val elementos: MutableList<Elemento> = mutableListOf<Elemento>()
    var id = 0

    fun create(elemento: Elemento){
        creacionCorrecta(elemento)
        yaEstaCreado(elemento)
        agregarAlRepo(elemento)
    }

    fun creacionCorrecta(elemento: Elemento){
        if(!elemento.completamenteValido()){
            throw BusinessException("NO ESTA BIEN CREADO")
        }
    }

    fun yaEstaCreado(elemento: Elemento){
        if(estaEnRepo(elemento)){
            throw BusinessException("YA ESTA EN EL REPO")
        }
    }
    fun agregarAlRepo(elemento: Elemento){
        elementos.add(elemento)
        elemento.id  = id
        id++
    }

    fun estaEnRepo(elemento: Elemento) = !elemento.esNuevo()

    fun delete(elemento: Elemento) {
        borradoImposible(elemento)
        elementos.remove(elemento)
    }

    /*fun update(elemeto: Elemento){
        var elementoModificado = elemeto.modificar()
        delete(elemeto)
        agregaElementoModificado(elementoModificado)
    }*/

    fun agregaElementoModificado(elementoModificado: Elemento) {
        creacionCorrecta(elementoModificado)
        elementos.add(elementoModificado)
    }

    fun borradoImposible(elemento: Elemento){
        if(!estaEnRepo(elemento)){
            throw BusinessException("NO ESTA EN EL REPO")
        }
    }

    fun getById(idABuscar: Int) = elementos.first { it.id == idABuscar  }

    fun listar(cadena: String) = elementos.filter { it.coincidencia(cadena) }
}