package ar.edu.unsam.algo

interface TipoUsuario{
    fun aceptaVehiculo(vehiculo: Vehiculo): Boolean
}

object Neofilo: TipoUsuario{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.antiguedad() < 2
}

object Supersticioso : TipoUsuario {
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.anioDeFabricacion.year % 2 == 0
}

object Caprichoso : TipoUsuario{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = compararMarcaModelo(vehiculo)

    fun compararMarcaModelo(vehiculo: Vehiculo) = obtenerInicial(vehiculo.marca) == obtenerInicial(vehiculo.modelo)

    fun obtenerInicial(cadena: String) = cadena.get(0)

}

class Selectivo(var marcaPreferida :String) : TipoUsuario{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = marcaPreferida.equals(vehiculo.marca,ignoreCase = true)
}

object SinLimite : TipoUsuario{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.kilometrajeLibre()
}

class Combinado(): TipoUsuario{

    var listaDeTipos = mutableListOf<TipoUsuario>()

    fun agregarTipo(tipoUsuario: TipoUsuario) = listaDeTipos.add(tipoUsuario)

    fun validarLista() = listaDeTipos.size >= 2

    override fun aceptaVehiculo(vehiculo: Vehiculo): Boolean{
        if(!validarLista()){
            throw Exception("Le hace falta mas tipos")
        }
        return listaDeTipos.all { it.aceptaVehiculo(vehiculo) }
    }
}