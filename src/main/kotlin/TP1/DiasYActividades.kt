package TP1

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

data class Actividad(var costo: Double, var descrpcion: String, var inicio: LocalTime, var fin: LocalTime, var dificultad: Int) {
    fun duracion() = ChronoUnit.MINUTES.between(fin,inicio)

    fun esValido() =
        (this.costo >= 0) and (this.dificultad > 0) and this.tieneInformacionCargadaEnDescripcion() and (this.inicio < this.fin)

    fun tieneInformacionCargadaEnDescripcion() = !this.descrpcion.isNullOrEmpty()
}