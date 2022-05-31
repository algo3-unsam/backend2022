package ar.edu.unsam.algo

interface Acciones {
    fun ejecutar(usuario: Usuario,viaje: Viaje)
}

class RealizaViajeLocal():Acciones{
    override fun ejecutar(usuario: Usuario,viaje: Viaje) {
        if(!viaje.esLocal()) usuario.cambiarCriterio(Localista)
    }

}

class MandarMailAAmigosQueDeseanDestino(var listaCorreo: ListaCorreo):Acciones{
    override fun ejecutar(usuario: Usuario,viaje: Viaje) {
        listaCorreo.sendMail(viaje, usuario)
    }

}

//Modificaciones de las funciones AgregarAListaDeItinerariosParaPuntuar() y RealizaViajeConConvenio() REVISAR
class AgregarAListaDeItinerariosParaPuntuar() : Acciones{
    override fun ejecutar(usuario: Usuario, viaje: Viaje) {
        if(usuario.estaParaPuntuar(viaje.itinerario)) throw BusinessException("El itinirario indicado ya esta para puntuar") else usuario.obtener(viaje.itinerario)
    }
}

class  RealizaViajeConConvenio() : Acciones{
    override fun ejecutar(usuario: Usuario, viaje: Viaje) {
        if(viaje.vehiculoConConvenio()) throw BusinessException("El viaje ya tiene un vehiculo con convenio") else cambiarCriterio(usuario, viaje)
    }

    fun cambiarCriterio(usuario: Usuario, viaje: Viaje){
        usuario.cambiarCriterioVehiculoA(Selectivo(viaje.vehiculo.marcasConConvenio().first()))
    }

}