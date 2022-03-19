import java.util.*
class Usuario(var Nombre: String, var Apellido: String, var Username: String, var fechaDeAlta: Int, var paisDeResidencia: String){
    fun antiguedad() = Calendar.YEAR - fechaDeAlta

    fun descuentoPorAntiguedad() = if(antiguedad() > 15) 15 else antiguedad()
}

class Destino(var Pais: String, var Ciudad: String, var costoBase: Float){

    fun esLocal() = Pais == "Argentina"

    fun precio(unUsuario: Usuario) = costoBase + porcentajeDestino() - descuento(unUsuario)

    fun porcentajeDestino() = if (!esLocal()) costoBase * 0.2 else 0.0

    fun descuento(unUsuario: Usuario) = costoBase * (unUsuario.descuentoPorAntiguedad()*0.01)
}
