package ar.edu.unsam.algo

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    var cantDias: Int,
    var dias: MutableList<Dia> = mutableListOf()
) {

    val puntajes = mutableMapOf<String, Int>()

    fun recibirPuntaje(unUsuario: Usuario, puntaje : Int){
        puntajes[unUsuario.username] = puntaje
    }

    fun yaPuntuo(username : String) = puntajes.containsKey(username)

    fun unDiaConActividad() = dias.any{ it.tengoActividades()}

    fun hayDiasInciados() = dias.isNotEmpty()

    fun existeDiaConActividadInciado() = this.hayDiasInciados() and this.unDiaConActividad()
    //fun tieneCreadorYDestino() = !this.creador.isNullorEmpty() and !this.destino.isNullorEmpty()

    fun validar(){
        if(!this.existeDiaConActividadInciado()){
            throw FaltaCargarInformacionException("El Itinerario no tiene ninguna actividad")
        }
    }

    fun totalCosto() = dias.sumOf { it.costoDeTotalDeActividades() }

    fun ocuparDia(undia: Dia) {
        dias.add(undia)
    }

    fun agregarActividadAlDia(undia: Dia, unaActividad: Actividad) {
        if (dias.contains(undia)) {
           undia.agregarActividad(unaActividad)
        } else {
            throw BusinessException("No se encontro el dia en el itinerario")
        }
    }

    //reviso que todos los dias asignados al itinerario tengan actividades
    fun todoLosDiasOcupados() = (dias.all { it.tengoActividades() } ) && todosLosDiasIniciados()

    fun todosLosDiasIniciados() = dias.size == this.cantDias

    fun sosMiCreador(unUsuario: Usuario) = unUsuario.username.equals(creador.username,ignoreCase = true)

    fun cantidadDeActividades() = dias.sumOf { it.cantidadDeActidades() }


    fun todasLasActividades():MutableList<Actividad>{
        var todasLasActividades: MutableList<Actividad> = mutableListOf()
        dias.forEach { todasLasActividades.addAll(it.actividades)}
        return todasLasActividades
    }

    fun actividadesXDificultad() = todasLasActividades().groupingBy { it.dificultad }.eachCount()

    fun mapDeActividadesOrdenado() = actividadesXDificultad().toSortedMap(compareByDescending { it })

    fun dificultad(): Dificultad = mapDeActividadesOrdenado().maxByOrNull { it.value }!!.key

    fun cantDeActividadesDeDifcultad(unaDificultad: Dificultad) = actividadesXDificultad()[unaDificultad]!!

    fun porcentajeDeActividadXDificultad(unaDificultad: Dificultad) =
        (cantDeActividadesDeDifcultad(unaDificultad) * 100) / cantidadDeActividades()

    fun verPuntaje(usuario: Usuario): Int{
        if(!puntajes.containsKey(usuario.username)){
            throw BusinessException("El usuario nunca puntuo el itinerario")
        }
        return puntajes.getValue(usuario.username)
    }

    fun editar(unUsuario: Usuario){
        if(!unUsuario.puedoEditar(this)){
            throw BusinessException("Este usuario no puede editar el itinerario")
        }
    }

    fun tieneDestinoLocal() = destino.esLocal()
}