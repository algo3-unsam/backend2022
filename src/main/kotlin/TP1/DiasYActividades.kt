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
        return actividades.all { (unaActividad.horarioInicio >= it.horarioFin && unaActividad.horarioFin>= it.horarioInicio)}
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
        actividades.sortBy { it.horarioInicio }
    }

    /*fun tieneEspacio(nuevaActividad: Actividad): Boolean {
        return actividades.all{this.puedeEntrar(it,nuevaActividad)}
    }

   fun puedeEntrar(it: Actividad, nuevaActividad: Actividad):Boolean {
        if(it.horarioInicio > nuevaActividad.horarioInicio){
            return it.horarioInicio >= nuevaActividad.horarioFin
        }
        return it.horarioFin < nuevaActividad.horarioInicio
    }*/
}

data class Actividad(var costo: Double, var descrpcion: String, var horarioInicio: LocalTime, var horarioFin: LocalTime, var dificultad: Int) {
    fun duracion() = ChronoUnit.MINUTES.between(horarioInicio,horarioFin).toInt()

    fun validarDificultad() = (this.dificultad >= Dificultades.baja.numero) and (this.dificultad <= Dificultades.alta.numero)

    fun tieneInformacionCargadaEnDescripcion() = !this.descrpcion.isNullOrEmpty()

    fun validarDuracion() = this.duracion() >= 0

    fun validarCosto() = costo >=0

    fun validar(){
        if(!this.validarDificultad() or !this.tieneInformacionCargadaEnDescripcion() or ! this.validarCosto() or !this.validarDuracion()){
            throw Exception("No se puede crear esta Actividad")
        }
    }
}