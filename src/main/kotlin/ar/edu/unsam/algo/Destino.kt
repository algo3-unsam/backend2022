package ar.edu.unsam.algo

import com.google.gson.annotations.SerializedName


class Destino(val pais: String, var ciudad: String, @SerializedName("costo") var costoBase: Float) : Datos {

    override var id = 0

    companion object {
        var LOCAL = "Argentina"
    }

    override fun validacion() {
        if (!this.esValido()) {
            throw FaltaCargarInformacionException("destino invalido,falta informacion\n costo base: $costoBase, pais: $pais, ciudad: $ciudad")
        }
    }

    override fun coincidencia(cadena: String): Boolean = pais.contains(cadena,ignoreCase = true) || ciudad.contains(cadena,ignoreCase = true)
    
    override fun esValido() = (this.costoBase > 0) && this.tieneInformacionCargadaEnStrings()

    fun tieneInformacionCargadaEnStrings() = !(this.pais.isNullOrEmpty() && this.ciudad.isNullOrEmpty())

    fun esLocal() = pais.equals(LOCAL, ignoreCase = true)

    fun precio(usuario: Usuario) = costoBase + porcentajeDestino() - descuento(usuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(usuario: Usuario) =
        if (usuario.esDelMismoPaisQueDestino((this))) (costoBase * (usuario.descuentoPorAntiguedad() * 0.01)) else 0.0

}
