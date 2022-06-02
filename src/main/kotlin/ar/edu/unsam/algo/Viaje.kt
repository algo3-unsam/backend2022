package ar.edu.unsam.algo

class Viaje(var vehiculo: Vehiculo, var itinerario : Itinerario){

    fun costoTotal(usuario: Usuario) = itinerario.totalCosto(usuario) + vehiculo.costoFinal(usuario.diasParaViajar)

    fun esLocal() = itinerario.tieneDestinoLocal()

    fun vehiculoConConvenio() = vehiculo.tieneConvenio()

    fun getDestino() = itinerario.destino

}