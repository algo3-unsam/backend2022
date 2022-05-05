package ar.edu.unsam.algo

interface Datos {
    var id : Int

    fun coincidencia(cadena: String) : Boolean

    fun esValido() : Boolean

    fun validacion()

}
