package ar.edu.unsam.algo

class Destino(var pais: String, var ciudad: String, var costoBase: Float){

    companion object {
        var LOCAL = "Argentina"
    }

    fun validar() {if(!this.esValido()){throw CustomException("No se puede crear esta Destino")}}
    
    fun esValido()= (this.costoBase > 0) && this.tieneInformacionCargadaEnStrings()

    fun tieneInformacionCargadaEnStrings() = !(this.pais.isNullOrEmpty() && this.ciudad.isNullOrEmpty())

    fun esLocal() = pais.equals(LOCAL, ignoreCase = true)

    fun precio(unUsuario: Usuario) = costoBase + porcentajeDestino() - descuento(unUsuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(unUsuario: Usuario) = if (unUsuario.esDelMismoPaisQueDestino((this))) (costoBase * (unUsuario.descuentoPorAntiguedad()*0.01)) else 0.0

}
