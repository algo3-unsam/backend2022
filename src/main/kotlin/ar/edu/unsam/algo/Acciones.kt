package ar.edu.unsam.algo

interface Acciones {
    fun ejecutar(usuario: Usuario,viaje: Viaje)
}

class AgregarAListaDeItinerariosParaPuntuar():Acciones{
    override fun ejecutar(usuario: Usuario,viaje: Viaje) {
        usuario.itinerariosUsuario.add(viaje.itinerario)
    }

}
class RealizaViajeLocal():Acciones{
    override fun ejecutar(usuario: Usuario,viaje: Viaje) {
        if(!viaje.esLocal()) usuario.cambiarCriterio(Localista)
    }

}
class RealizaViajeConConvenio():Acciones{
    override fun ejecutar(usuario: Usuario,viaje: Viaje) {
        if(!viaje.vehiculoConConvenio()) usuario.cambiarCriterioVehiculoA(Selectivo("Honda"))
    }

}
class MandarMailAAmigosQueDeseanDestino():Acciones{
    override fun ejecutar(usuario: Usuario,viaje: Viaje) {
        "Hacer"
    }

}