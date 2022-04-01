package TP1

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class Dificultades(val numero: Int){
    baja(1),
    media(2),
    alta(3)
}

class Dia(var actividades: MutableList<Actividad> = mutableListOf()) {
    fun verificarHorario(unaActividad: Actividad): Boolean {
        //debo verificar que el horario de inicio de la actividad a agendar sea mayor al del horario final de las demas actividades
        return actividades.any { (unaActividad.inicio >= it.fin) }
    }

    fun agregarActividad(unaActividad: Actividad) {
        //primero debo revisar que mi lista de actividades para el dia no este vacia, en ese caso
        if (actividades.isEmpty()) {
            actividades.add(unaActividad)

        } else if (verificarHorario(unaActividad)) {
            actividades.add(unaActividad)

        } else {
            throw Exception("No se puede agregar la actividad porque el horario esta ocupado")
        }
    }
}

class Itinerario(
    var creador: Usuario,
    var destino: Destino,
    var cantDias: Int,
    var dias: MutableList<Dia> = mutableListOf()
) {
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

data class Actividad(var costo: Double, var descrpcion: String, var inicio: LocalTime, var fin: LocalTime, var dificultad: Int) {
    fun duracion() = ChronoUnit.MINUTES.between(fin,inicio)

    fun esValido() =
        (this.costo >= 0) and (this.dificultad > 0) and this.tieneInformacionCargadaEnDescripcion() and (this.inicio < this.fin)

    fun tieneInformacionCargadaEnDescripcion() = !this.descrpcion.isNullOrEmpty()
}