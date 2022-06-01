package ar.edu.unsam.algo

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    //var cantDias: Int,
    val dias: MutableList<Dia> = mutableListOf()
): Datos{
    override var id: Int = 0

    val puntajes = mutableMapOf<String, Int>()

    fun recibirPuntaje(usuario: Usuario, puntaje : Int){
        puntajes[usuario.username] = puntaje
    }

    fun yaPuntuo(username : String) = puntajes.containsKey(username)

    fun unDiaConActividad() = dias.any{ it.tengoActividades()}

    fun hayDiasInciados() = dias.isNotEmpty()

    fun cantidadDeDias() = dias.size

    override fun esValido() = this.hayDiasInciados() and this.unDiaConActividad()

    override fun validacion(){
        if(!this.esValido()){
            throw FaltaCargarInformacionException("El Itinerario no tiene ninguna actividad")
        }
    }


    override fun coincidencia(cadena: String): Boolean = destino.coincidencia(cadena) || coincidenciaConActividades(cadena)

    fun coincidenciaConActividades(cadena:String) = todasLasActividades().any{it.coincidenciaConNombre(cadena)}

    fun totalCosto(usuario: Usuario) = dias.sumOf { it.costoDeTotalDeActividades() } + destino.precio(usuario)

    fun ocuparDia(dia: Dia) {
        dias.add(dia)
    }

    fun agregarActividadAlDia(dia: Dia, actividad: Actividad) {
        if (!dias.contains(dia)) {
            throw BusinessException("No se encontro el dia en el itinerario")
        }
        dia.agregarActividad(actividad)
    }

    //reviso que todos los dias asignados al itinerario tengan actividades
    fun todoLosDiasOcupados() = (dias.all { it.tengoActividades() } )


    fun sosMiCreador(usuario: Usuario) = usuario.username.equals(creador.username,ignoreCase = true)

    fun cantidadDeActividades() = dias.sumOf { it.cantidadDeActidades() }

    fun todasLasActividades(): MutableList<Actividad> {
        return dias.flatMap { it.actividades }.toMutableList()
    }

    fun actividadesXDificultad() = todasLasActividades().groupingBy { it.dificultad }.eachCount()

    fun mapDeActividadesOrdenado() = actividadesXDificultad().toSortedMap(compareByDescending { it })

    fun dificultad(): Dificultad = mapDeActividadesOrdenado().maxByOrNull { it.value }!!.key

    fun cantDeActividadesDeDifcultad(dificultad: Dificultad) = actividadesXDificultad()[dificultad]!!

    fun porcentajeDeActividadXDificultad(dificultad: Dificultad) =
        (cantDeActividadesDeDifcultad(dificultad) * 100) / cantidadDeActividades()

    fun verPuntaje(usuario: Usuario): Int{
        if(!puntajes.containsKey(usuario.username)){
            throw BusinessException("El usuario nunca puntuo el itinerario")
        }
        return puntajes.getValue(usuario.username)
    }

    fun editar(usuario: Usuario){
        if(!usuario.puedoEditar(this)){
            throw BusinessException("Este usuario no puede editar el itinerario")
        }
    }

    fun tieneDestinoLocal() = destino.esLocal()

    fun cambiarCreador(unUsuario: Usuario) {creador = unUsuario}
}