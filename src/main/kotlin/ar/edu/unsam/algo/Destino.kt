package ar.edu.unsam.algo

import com.google.gson.annotations.SerializedName

class Destino(val pais: String, var ciudad: String, @SerializedName("costo")var costoBase: Float):Datos {
    override var id = 0

    companion object {
        var LOCAL = "Argentina"
    }

    fun validacion() {
        if (!this.completamenteValido()) {
            throw FaltaCargarInformacionException("destino invalido,falta informacion\n costo base: $costoBase, pais: $pais, ciudad: $ciudad")
        }
    }

    override fun coincidencia(cadena: String): Boolean = coincidenciaParcial(pais,cadena) || coincidenciaParcial(ciudad,cadena)

    override fun completamenteValido() = (this.costoBase > 0) && this.tieneInformacionCargadaEnStrings()

    fun tieneInformacionCargadaEnStrings() = !(this.pais.isNullOrEmpty() && this.ciudad.isNullOrEmpty())

    fun esLocal() = pais.equals(LOCAL, ignoreCase = true)

    fun precio(usuario: Usuario) = costoBase + porcentajeDestino() - descuento(usuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(usuario: Usuario) =
        if (usuario.esDelMismoPaisQueDestino((this))) (costoBase * (usuario.descuentoPorAntiguedad() * 0.01)) else 0.0

    override fun toString(): String = "id: $id, Pais: $pais, Ciudad: $ciudad, Costo: $costoBase "
}
