package ar.edu.unsam.algo

class Repositorio<Elemento : Datos> {
    val elementos: MutableMap<Int,Elemento> = mutableMapOf<Int,Elemento>()
    var id = 0

    fun create(elemento: Elemento){
        elementos[id] = elemento
        id++
    }

   // fun delete(elemento: Elemento) = elementos.remove()

    //fun obtenerId(elemento: Elemento)  =  elementos.
    //fun update(elemento: Elemento) =

    fun getById(id: Int) = elementos[id]

    fun listar(cadena: String) = elementos.filter { it.value.coincidencia(cadena) }
}