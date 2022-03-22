import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.Year
import java.util.*
class Usuario(var Nombre: String, var Apellido: String, var Username: String, var fechaDeAlta: String, var paisDeResidencia: String){
    fun antiguedad() = Period.between(LocalDate.parse(fechaDeAlta), LocalDate.now())

    fun descuentoPorAntiguedad() = if(antiguedad().years > 15) 15 else antiguedad().years
}

class Destino(var Pais: String, var Ciudad: String, var costoBase: Float){

    fun esLocal() = Pais == "Argentina"

    fun precio(unUsuario: Usuario) = costoBase + porcentajeDestino() - descuento(unUsuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(unUsuario: Usuario) = if (unUsuario.paisDeResidencia == Pais) (costoBase * (unUsuario.descuentoPorAntiguedad()*0.01)) else 0.0
}
