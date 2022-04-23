package ar.edu.unsam.algo

interface CriterioVehiculo{
    fun aceptaVehiculo(vehiculo: Vehiculo): Boolean
}

object Neofilo: CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.antiguedad() < 2
}

object Supersticioso : CriterioVehiculo {
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.anioDeFabricacion.year % 2 == 0
}

object Caprichoso : CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = compararMarcaModelo(vehiculo)

    fun compararMarcaModelo(vehiculo: Vehiculo) = obtenerInicial(vehiculo.marca) == obtenerInicial(vehiculo.modelo)

    fun obtenerInicial(cadena: String) = cadena.get(0)

}

class Selectivo(var marcaPreferida :String) : CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = marcaPreferida.equals(vehiculo.marca,ignoreCase = true)
}

object SinLimite : CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.kilometrajeLibre
}

class Combinado(): CriterioVehiculo{

    var listaDeTipos = mutableListOf<CriterioVehiculo>()

    fun agregarTipo(tipoUsuario: CriterioVehiculo) = listaDeTipos.add(tipoUsuario)

    fun validarLista() = listaDeTipos.size >= 2

    override fun aceptaVehiculo(vehiculo: Vehiculo): Boolean{
        if(!validarLista()){
            throw Exception("Le hace falta mas tipos")
        }
        return listaDeTipos.all { it.aceptaVehiculo(vehiculo) }
    }
}