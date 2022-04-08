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

    fun unDiaConActividad() = dias.any{ it.actividades.size > 0}

    fun hayDiasInciados() = dias.isNotEmpty()

    fun existeDiaConActividadInciado() = this.hayDiasInciados() and this.unDiaConActividad()
    //fun tieneCreadorYDestino() = !this.creador.isNullorEmpty() and !this.destino.isNullorEmpty()

    fun validar(){
        if(!this.existeDiaConActividadInciado()){
            throw FaltaCargarInformacion("El Itinerario no tiene ninguna actividad")
        }
    }

    fun totalCosto() = dias.sumOf { it.costoDeTotalDeActividades() }

    fun ocuparDia(undia: Dia) {
        dias.add(undia)
    }

    fun agregarActividad(undia: Dia, unaActividad: Actividad) {
        //obtengo el indice del dia correspondiente y se revisa si puede agregarse la actividad
        if (dias.contains(undia)) {
            dias[dias.indexOf(undia)].agregarActividad(unaActividad)
        } else {
            throw FaltaCargarInformacion("No se encontro el dia en el itinerario")
        }
    }

    //reviso que todos los dias asignados al itinerario tengan actividades
    fun todoLosDiasOcupados() = (dias.all { it.cantindadDeActidades() > 0 } ) && todosLosDiasIniciados()

    fun todosLosDiasIniciados() = dias.size == this.cantDias

    fun sosMiCreador(unUsuario: Usuario) = unUsuario.username.equals(creador.username,ignoreCase = true)

    fun cantidadDeActividades() = dias.sumOf { it.cantindadDeActidades() }

    fun actividadesTotalDificultad(unaDificultad: Dificultades) = dias.flatMap { dia -> dia.actividadesDeUnTipo(unaDificultad) }.size

    fun dificultad(): Dificultades {

        if (actividadesTotalDificultad(Dificultades.ALTA) >= actividadesTotalDificultad(Dificultades.MEDIA)) {
            return if (actividadesTotalDificultad(Dificultades.ALTA) >= actividadesTotalDificultad(Dificultades.BAJA)) {
                return Dificultades.ALTA
            } else
                return Dificultades.BAJA
        } else if (actividadesTotalDificultad(Dificultades.MEDIA) >= actividadesTotalDificultad(Dificultades.BAJA)) {
            return Dificultades.MEDIA
        }
        return Dificultades.BAJA
    }

    fun porcentajeDeActividadXDificultad(unaDificultad: Dificultades) =
        (actividadesTotalDificultad(unaDificultad) * 100) / cantidadDeActividades()

    fun verPuntaje(usuario: Usuario): Int{
        if(!puntajes.containsKey(usuario.username)){
            throw FaltaCargarInformacion("El usuario nunca lo puntuo")
        }
        return puntajes.getValue(usuario.username)
    }
}