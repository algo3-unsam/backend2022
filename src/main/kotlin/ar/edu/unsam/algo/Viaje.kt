package ar.edu.unsam.algo

class Viaje(var tieneConvenioConVehiculo: Boolean, var itinerario: Itinerario, val listaDestinosParaViaje: MutableList<Destino> = mutableListOf()) {

    fun agregarDestino(destino: Destino) = listaDestinosParaViaje.add(destino)

    fun totalCostoViaje() = listaDestinosParaViaje.sumOf { it.costoBase.toDouble() }

    fun esLocal() = listaDestinosParaViaje.any{it.esLocal()}

}