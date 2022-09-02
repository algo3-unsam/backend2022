package ar.edu.unsam.algo

import java.time.LocalDate
import java.time.temporal.ChronoUnit

interface Vehiculo:Datos{

    val marca: String
    val modelo: String
    val anioDeFabricacion: LocalDate
    var costoDiario: Double
    val kilometrajeLibre: Boolean
    override var id: Int

    companion object{
        var marcasConvenio: MutableList<String> = mutableListOf("Honda")
        fun primeraMarcaConConvenio() = marcasConvenio.first()
    }

    fun costoBase(diasDealquiler: Int) = costoDiario * diasDealquiler

    fun costoFinal(diasDealquiler: Int) = (costoBase(diasDealquiler) + aumentoPorCondicion(diasDealquiler)) * factorDescuentoPorConvenio(diasDealquiler)

    fun tieneConvenio() = containsString(marcasConvenio,marca)

    fun factorDescuentoPorConvenio(diasDealquiler: Int) = if(tieneConvenio())  0.9 else 1.0

    fun antiguedad() = ChronoUnit.YEARS.between(anioDeFabricacion, LocalDate.now())

    fun aumentoPorCondicion(diasDealquiler: Int) : Double

    override fun coincidencia(cadena: String) = marca.equals(cadena,ignoreCase = true) or comienzoIgual(cadena)

    fun comienzoIgual(cadena: String) = modelo.startsWith(cadena,ignoreCase = true)

    fun esModerno()= antiguedad() < 2

    fun anioDeFabricacionPar() = anioDeFabricacion.year % 2 == 0

    fun coincidenInicialMarcaModelo() = marca.first() == modelo.first()

    fun marcasConConvenio() = marcasConvenio

    override fun esValido() = this.tieneInformacionCargadaEnStrings() && this.costoDiarioValido() && this.tieneFechaAltaValida()


    fun tieneInformacionCargadaEnStrings() = !(this.marca.isNullOrEmpty() && this.modelo.isNullOrEmpty())

    fun tieneFechaAltaValida(): Boolean = this.anioDeFabricacion > LocalDate.now()

    fun costoDiarioValido() = costoDiario > 0

    override fun validacion() {
        if (!this.esValido()) {
            throw FaltaCargarInformacionException("destino invalido,falta informacion\n Marca: $marca ,Modelo: $modelo , Año de Fabricacion: $anioDeFabricacion , costo diario : $costoDiario")

        }
    }
}

class Moto(
    override val marca: String,
    override val modelo: String,
    override val anioDeFabricacion: LocalDate,
    override var costoDiario: Double,
    override val kilometrajeLibre: Boolean,
    val cantidadCilindrada: Int,
    override var id: Int = 0,

): Vehiculo {

    fun extraPorDia(diasDealquiler: Int): Double = (diasDealquiler * 500.0)

    override fun aumentoPorCondicion(diasDealquiler: Int): Double {
        return if (superaCilindradasMinimas()) extraPorDia(diasDealquiler) else 0.0
    }

    fun superaCilindradasMinimas() = cantidadCilindrada > 250
}

class Auto(
    override val marca: String,
    override val modelo: String,
    override val anioDeFabricacion: LocalDate,
    override var costoDiario: Double,
    override val kilometrajeLibre: Boolean,
    val hatchback: Boolean,
    override var id: Int = 0,
): Vehiculo {

    override fun aumentoPorCondicion(diasDealquiler: Int): Double {
        return  costoBase(diasDealquiler) * porcentaje()
    }

    fun porcentaje() = if (!hatchback) 0.25 else 0.1

}

class Camioneta(
    override val marca: String,
    override val modelo: String,
    override val anioDeFabricacion: LocalDate,
    override var costoDiario: Double,
    override val kilometrajeLibre: Boolean,
    val todoTerreno: Boolean,
    override var id: Int = 0,
): Vehiculo{


    fun aumentoPorExceso(diasDealquiler: Int) = 10000 + Math.max(0, diasDealquiler - 7)*1000

    fun aumentoPorTodoTerreno(diasDealquiler: Int) = if(todoTerreno) costoBase(diasDealquiler) * 0.5 else 0.0

    override fun aumentoPorCondicion(diasDealquiler: Int): Double {
        return aumentoPorExceso(diasDealquiler) + aumentoPorTodoTerreno(diasDealquiler)
    }


}