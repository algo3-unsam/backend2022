package ar.edu.unsam.algo

interface CriterioVehiculo{
    fun aceptaVehiculo(vehiculo: Vehiculo): Boolean
}

class Neofilo(var usuario: Usuario): CriterioVehiculo{
    override fun aceptaVehiculo(vehiculo: Vehiculo) = usuario.leGustaLosVehiculosAntiguos(vehiculo)
}

class Supersticioso(var usuario: Usuario) : CriterioVehiculo {
    override fun aceptaVehiculo(vehiculo: Vehiculo) = usuario.soySupersticioso(vehiculo)
}