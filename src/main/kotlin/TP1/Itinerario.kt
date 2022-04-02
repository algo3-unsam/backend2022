package TP1

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    var cantDias: Int,
    var dias: MutableList<Dia> = mutableListOf()
) {

    fun unDiaConActividad() = dias.any{ it.actividades.size > 0}

    //fun tieneCreadorYDestino() = !this.creador.isNullorEmpty() and !this.destino.isNullorEmpty()

    fun validar(){
        if(!this.unDiaConActividad()){
            throw Exception("Este Itinerario es Invalido")
        }
    }

    fun totalCosto() = dias.sumOf { dia -> (dia.actividades.sumOf { it.costo }) }

    fun ocuparDia(undia: Dia) {
        dias.add(undia)
    }

    fun agregarActividad(undia: Dia, unaActividad: Actividad) {
        //obtengo el indice del dia correspondiente y se revisa si puede agregarse la actividad
        if (dias.contains(undia)) {
            dias[dias.indexOf(undia)].agregarActividad(unaActividad)
        } else {
            throw Exception("No se encontro el dia en el itinerario")
        }
    }

    //reviso que todos los dias asignados al itinerario tengan actividades
    fun todoLosDiasOcupados() = dias.all { it.actividades.size > 0 }

    fun sosMiCreador(unUsuario: Usuario) = (unUsuario.username == creador.username)

    fun cantidadDeActividades() = dias.map { dia -> dia.actividades }.size

    fun actividadesTotalDificultad(unaDificultad: Int) = dias.flatMap { dia -> dia.actividades.filter { it.dificultad == unaDificultad } }.size

    fun dificultad(): Int {

        if (actividadesTotalDificultad(Dificultades.alta.numero) >= actividadesTotalDificultad(Dificultades.media.numero)) {
            return if (actividadesTotalDificultad(Dificultades.alta.numero) >= actividadesTotalDificultad(Dificultades.baja.numero)) {
                Dificultades.alta.numero
            } else
                Dificultades.baja.numero
        } else if (actividadesTotalDificultad(Dificultades.media.numero) >= actividadesTotalDificultad(Dificultades.baja.numero)) {
            Dificultades.media.numero
        }
        return Dificultades.baja.numero
    }

    fun porcentajeDeActividadXDificultad(unaDificultad: Int) =
        ((dias.flatMap { dia -> dia.actividades.filter { it.dificultad == unaDificultad } }.size) * 100) / cantidadDeActividades()
}