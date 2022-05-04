package ar.edu.unsam.algo

interface Datos {
    public var id : Int
    fun coincidencia(cadena: String) : Boolean

    fun coincidenciaParcial(atributoPropio: String,cadena: String) = atributoPropio.contains(cadena,ignoreCase = true)

    fun esValido() : Boolean

    fun validacion()
}
