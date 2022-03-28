package TP1

import java.time.LocalDate

val ALTA = 3
val MEDIA  = 2
val BAJA = 1


class Dia(var nombre:String ,var actividades: MutableList<Actividad> = mutableListOf()){
    fun verificarHorario(unaActividad: Actividad): Boolean {
        //debo verificar que el horario de inicio y final de la actividad a agregar sea distinto de las otras actividades del dia
        return actividades.any{(unaActividad.inicio >= it.fin )}

    }

    fun agregarActividad(unaActividad: Actividad){
        //primero debo revisar que mi lista de actividades para el dia no este vacia, en ese caso
        if(actividades.isEmpty()){
            actividades.add(unaActividad)

        }else if (verificarHorario(unaActividad)){
            actividades.add(unaActividad)

        }else{
            throw Exception("No se puede agregar la actividad porque el horario esta ocupado")
        }


    }
}

class Itinerario(var creador: Usuario, var destino: Destino, var cantDias: Int, var dias: MutableList<Dia> = mutableListOf()){
    fun totalCosto() = dias.sumOf{ dia -> (dia.actividades.sumOf { it.costo })}

    fun ocuparDia(undia: Dia){
        dias.add(undia)
    }
    fun agregarActividad(undia: Dia, unaActividad: Actividad){
        //obtengo el indice del dia correspondiente y se revisa si puede agregarse la actividad
        if(dias.contains(undia)){
            dias[dias.indexOf(undia)].agregarActividad(unaActividad)
        }else{
            throw Exception("No se encontro el dia en el itinerario")
        }

    }

    //reviso que todos los dias asignados al itinerario tengan actividades
    fun todoLosDiasOcupados() = dias.any { it.actividades.size > 0 }

    fun sosMiCreador(unUsuario: Usuario) = (unUsuario.username == creador.username )

    fun cantidadDeActividades() = dias.map{ dia -> dia.actividades}.size

    fun actividadesDifAlta() = dias.flatMap { dia -> dia.actividades.filter { it.dificultad == ALTA} }.size

    fun actividadesDifMedia() = dias.flatMap { dia -> dia.actividades.filter { it.dificultad == MEDIA} }.size

    fun actividadesDifBaja() = dias.flatMap { dia -> dia.actividades.filter { it.dificultad == BAJA} }.size

    fun dificultad(): Int {
        //creo una nueva coleccion con map, revisando cada actividad de cada dia buscando la dificultad
        var actividadAlta = actividadesDifAlta()
        var actividadMedia = actividadesDifMedia()
        var actividadBaja = actividadesDifBaja()
        if (actividadAlta >= actividadMedia){
            return if(actividadAlta >= actividadBaja){
                ALTA
            } else
                BAJA
        }
        else if(actividadMedia>=actividadBaja){
            return MEDIA
        }
        return BAJA
    }

    fun porcentajeDeActividadXDificultad(unaDificultad: Int) =
        ((dias.map{ dia -> dia.actividades.filter{it.dificultad == ALTA}}.size) * 100) / cantidadDeActividades()



}


data class Actividad(var costo: Double, var descrpcion: String, var inicio: Int, var fin: Int, var dificultad : Int){
    fun duracion() = fin - inicio

    fun esValido() = (this.costo >= 0) and (this.dificultad > 0)  and this.tieneInformacionCargadaEnDescripcion() and (this.inicio < this.fin)

    fun tieneInformacionCargadaEnDescripcion() = !this.descrpcion.isNullOrEmpty()
}