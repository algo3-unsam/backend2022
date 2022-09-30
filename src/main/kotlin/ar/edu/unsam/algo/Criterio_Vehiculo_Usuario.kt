package ar.edu.unsam.algo

interface CriterioVehiculo{
    fun aceptaVehiculo(vehiculo: Vehiculo): Boolean
}

object Neofilo: CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.esModerno()
}

object Supersticioso : CriterioVehiculo {
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.anioDeFabricacionPar()
}

object Caprichoso : CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.coincidenInicialMarcaModelo()


}

class Selectivo(private val marcaPreferida :String) : CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = marcaPreferida.equals(vehiculo.marca,ignoreCase = true)
}

object SinLimite : CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = vehiculo.kilometrajeLibre
}

class Combinado : CriterioVehiculo{

    private var listaDeTipos = mutableListOf<CriterioVehiculo>()

    fun agregarTipo(tipoUsuario: CriterioVehiculo) = listaDeTipos.add(tipoUsuario)

    private fun validarLista() = listaDeTipos.size >= 2

    override fun aceptaVehiculo(vehiculo: Vehiculo): Boolean{
        if(!validarLista()){
            throw Exception("Le hace falta mas tipos")
        }
        return listaDeTipos.all { it.aceptaVehiculo(vehiculo) }
    }
}