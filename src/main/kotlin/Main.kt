
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit

class Usuario(var nombre: String, var apellido: String, var username: String, var fechaDeAlta: LocalDate, var paisDeResidencia: String){
    companion object{
        var ANTIGUEDAD_MAXIMA = 15
    }
    fun antiguedad() = ChronoUnit.YEARS.between(fechaDeAlta,LocalDate.now())//Period.between(LocalDate.parse(fechaDeAlta), LocalDate.now())

    fun descuentoPorAntiguedad() = if(antiguedad() > ANTIGUEDAD_MAXIMA) 15 else antiguedad()

    fun esDelMismoPaisQueDestino(unDestino: Destino) = this.paisDeResidencia == unDestino.pais
}

class Destino(var pais: String, var ciudad: String, var costoBase: Float){

    companion object{
        var LOCAL = "Argentina"
    }


    fun esLocal() = pais == LOCAL

    fun precio(unUsuario: Usuario) = costoBase + porcentajeDestino() - descuento(unUsuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(unUsuario: Usuario) = if (unUsuario.esDelMismoPaisQueDestino((this))) (costoBase * (unUsuario.descuentoPorAntiguedad()*0.01)) else 0.0

}

