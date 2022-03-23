import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.Year
import java.time.temporal.ChronoUnit
import java.util.*
class Usuario(var Nombre: String, var Apellido: String, var Username: String, var fechaDeAlta: LocalDate, var paisDeResidencia: String){
    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta, LocalDate.now())

    fun descuentoPorAntiguedad() = if(antiguedad() > 15) 15 else antiguedad()
}

class Destino(var Pais: String, var Ciudad: String, var costoBase: Float){
    companion object{
        var LOCAL = "Argentina"
    }

    fun esLocal() = Pais == LOCAL

    fun precio(unUsuario: Usuario) = costoBase + porcentajeDestino() - descuento(unUsuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(unUsuario: Usuario) = if (unUsuario.paisDeResidencia == Pais) (costoBase * (unUsuario.descuentoPorAntiguedad()*0.01)) else 0.0
}
