package ar.edu.unsam.algo

// NOTA: Si el viaje no tiene un vehículo con convenio, el usuario se convierte en selectivo,
// y le pasan a gustar los vehículos de una marca con convenio
// ** esto es un observer pa´ mi

class Viaje(var vehiculo: Vehiculo, var itinerario : Itinerario){

    fun costoTotal(usuario: Usuario) = itinerario.totalCosto(usuario) + vehiculo.costoFinal(usuario.diasParaViajar)

    fun esLocal() = itinerario.tieneDestinoLocal()

    fun vehiculoConConvenio() = vehiculo.tieneConvenio()
}