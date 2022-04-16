package ar.edu.unsam.algo

import java.time.LocalDate
import java.time.temporal.ChronoUnit

interface Vehiculo:Datos{

    val marca: String
    val modelo: String
    val anioDeFabricacion: LocalDate
    var costoDiario: Double

    companion object{var marcaConvenio: MutableList<String> = mutableListOf("Honda")}

    fun costoBase(diasDealquiler: Int) = costoDiario * diasDealquiler

    fun costoFinal(diasDealquiler: Int) = costoBase(diasDealquiler) - descuentoPorConvenio(diasDealquiler)

    fun tieneConvenio() = containsString(marcaConvenio,marca)

    fun descuentoPorConvenio(diasDealquiler: Int) = if(tieneConvenio()) costoBase(diasDealquiler) * 0.1 else 0.0

    fun antiguedad() = ChronoUnit.YEARS.between(anioDeFabricacion, LocalDate.now())

    fun kilometrajeLibre(): Boolean

    override fun coincidencia(cadena: String) = marca.equals(cadena,ignoreCase = true)

}

class Moto(
    override val marca: String,
    override val modelo: String,
    override val anioDeFabricacion: LocalDate,
    override var costoDiario: Double,
    val cantidadCilindrada: Int
): Vehiculo {
    override fun kilometrajeLibre() = true

    override fun costoBase(diasDealquiler: Int): Double = if(cilindradasMaximas()) super.costoBase(diasDealquiler) + extraPorDia(diasDealquiler) else super.costoBase(diasDealquiler)

    fun extraPorDia(diasDealquiler: Int) = (diasDealquiler * 500)


    fun cilindradasMaximas() = cantidadCilindrada > 250
}

class Auto(
    override val marca: String,
    override val modelo: String,
    override val anioDeFabricacion: LocalDate,
    override var costoDiario: Double,
    val hatchback: Boolean
): Vehiculo {
    var extra = 1.1
    override fun kilometrajeLibre() = true

    override fun costoBase(diasDealquiler: Int):Double {
        if(!hatchback){
            extra = 1.25
        }
        else {
            extra = 1.1
        }
        return super.costoBase(diasDealquiler) * extra
    }
}

class Camioneta(
    override val marca: String,
    override val modelo: String,
    override val anioDeFabricacion: LocalDate,
    override var costoDiario: Double,
    val todoTerreno: Boolean
): Vehiculo{
    override fun kilometrajeLibre() = true

    override fun costoBase(diasDealquiler: Int): Double = semanaDeAlquiler(diasDealquiler) + ((aumentoPorExceso(diasDealquiler)) * 1000)

    override fun costoFinal(diasDealquiler: Int): Double = costoBase(diasDealquiler) + aumentoPorTodoTerreno(diasDealquiler) - descuentoPorConvenio(diasDealquiler)

    fun semanaDeAlquiler(diasDealquiler: Int) = super.costoBase(diasDealquiler) + 10000

    fun aumentoPorExceso(diasDealquiler: Int) = Math.max(0, diasDealquiler - 7)

    fun aumentoPorTodoTerreno(diasDealquiler: Int) = if(todoTerreno) costoBase(diasDealquiler) * 0.5 else 0.0
}