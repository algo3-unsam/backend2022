package ar.edu.unsam.algo

interface Datos {
    public var id : Int
    fun coincidencia(cadena: String) : Boolean

    fun esNuevo()  = id == 0

    fun coincidenciaParcial(atributoPropio: String,cadena: String) = atributoPropio.contains(cadena,ignoreCase = true)
    fun completamenteValido() : Boolean
}
