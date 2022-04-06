package ar.edu.unsam.algo

import java.rmi.activation.ActivationID
import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class Dificultades(){
    BAJA,
    MEDIA,
    ALTA
}

class Dia(var actividades: MutableList<Actividad> = mutableListOf()) {

    fun verificarHorario(unaActividad: Actividad): Boolean {
        return actividades.all { compararHorarios(unaActividad, it)}
    }

    fun compararHorarios(actividadAEvaluar: Actividad, actividades: Actividad) =  compararInicioYFin(actividadAEvaluar, actividades) && compararFinEInicio(actividadAEvaluar, actividades)

    fun compararInicioYFin(actividadAEvaluar: Actividad, actividades: Actividad) = actividades.horarioInicio.bettwen(actividadAEvaluar.horarioFin)

    fun compararFinEInicio(actividadAEvaluar: Actividad, actividades: Actividad) = actividades.horarioFin.bettwen(actividadAEvaluar.horarioInicio)

    fun agregarActividad(unaActividad: Actividad) {
        //primero debo revisar que mi lista de actividades para el dia no este vacia, en ese caso
        if (actividades.isEmpty()) {
            actividades.add(unaActividad)

        } else if (verificarHorario(unaActividad)) {
            actividades.add(unaActividad)

        } else {
            throw CustomException("No se puede agregar la actividad porque el horario esta ocupado")
        }
        actividades.sortBy { it.horarioInicio }
    }

    fun cantindadDeActidades() = actividades.size

    fun costoDeTotalDeActividades() = actividades.sumOf {it.costo }

    fun actividadesDeUnTipo(unaDificultad: Dificultades) = actividades.filter { it.dificultad == unaDificultad }

}

data class Actividad(var costo: Double, var descrpcion: String, var horarioInicio: LocalTime, var horarioFin: LocalTime, var dificultad: Dificultades) {
    fun duracion() = ChronoUnit.MINUTES.between(horarioInicio,horarioFin).toInt()

    fun validarDificultad() = (this.dificultad >= Dificultades.BAJA) && (this.dificultad <= Dificultades.ALTA)

    fun tieneInformacionCargadaEnDescripcion() = !this.descrpcion.isNullOrEmpty()

    fun validarDuracion() = this.duracion() >= 0

    fun validarCosto() = costo >=0

    fun validar(){
        if(!this.validarDificultad() or !this.tieneInformacionCargadaEnDescripcion() || ! this.validarCosto() || !this.validarDuracion()){
            throw CustomException("No se puede crear esta Actividad")
        }
    }

}