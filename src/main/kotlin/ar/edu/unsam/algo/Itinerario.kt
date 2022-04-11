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

    fun agregarActividad(undia: Dia, unaActividad: Actividad) {
        if (dias.contains(undia)) {
           undia.agregarActividadAlDia(unaActividad)
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

    fun actividadesXDificultad():MutableMap<Dificultad,Int>{
        var mapDeDificultades = todasLasActividades().groupingBy { it.dificultad }.eachCount().toMutableMap()
        enumValues<Dificultad>().forEach { rellenarDificultades(mapDeDificultades,it) }
        return mapDeDificultades
    }

    fun dificultad(): Dificultad {
        var dificultadSobrevivienteAltaMedia = dificultadConMasActividades(this,Dificultad.ALTA,Dificultad.MEDIA)
        return dificultadConMasActividades(this,dificultadSobrevivienteAltaMedia,Dificultad.BAJA)
    }

    fun porcentajeDeActividadXDificultad(unaDificultad: Dificultad) =
        (actividadesXDificultad()[unaDificultad]!! * 100) / cantidadDeActividades()

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