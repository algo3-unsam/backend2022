package TP1

class Destino(var pais: String, var ciudad: String, var costoBase: Float){

    companion object{
        var LOCAL = "Argentina"
    }

    fun validar() {if(!this.esValido()){throw Exception("No se puede crear esta Destino")}}
    
    fun esValido()= (this.costoBase > 0) and this.tieneInformacionCargadaEnStrings()

    fun tieneInformacionCargadaEnStrings() = !(this.pais.isNullOrEmpty() and this.ciudad.isNullOrEmpty())

    fun esLocal() = pais == LOCAL

    fun precio(unUsuario: Usuario) = costoBase + porcentajeDestino() - descuento(unUsuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(unUsuario: Usuario) = if (unUsuario.esDelMismoPaisQueDestino((this))) (costoBase * (unUsuario.descuentoPorAntiguedad()*0.01)) else 0.0
}
