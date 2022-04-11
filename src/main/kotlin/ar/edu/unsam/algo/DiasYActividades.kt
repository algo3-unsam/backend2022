package ar.edu.unsam.algo

import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class Dificultad() {
    BAJA,
    MEDIA,
    ALTA
}

class Dia(var actividades: MutableList<Actividad> = mutableListOf()) {

    fun verificarHorario(unaActividad: Actividad): Boolean {
        return actividades.all { compararHorarios(unaActividad, it) }
    }

    fun compararHorarios(actividadAEvaluar: Actividad, actividades: Actividad) =
        compararInicioYFin(actividadAEvaluar, actividades) && compararFinEInicio(actividadAEvaluar, actividades)

    fun compararInicioYFin(actividadAEvaluar: Actividad, actividades: Actividad) =
        actividades.horarioInicio.bettwen(actividadAEvaluar.horarioFin)

    fun compararFinEInicio(actividadAEvaluar: Actividad, actividades: Actividad) =
        actividades.horarioFin.bettwen(actividadAEvaluar.horarioInicio)

    fun agregarActividadAlDia(unaActividad: Actividad) {
        if (verificarHorario(unaActividad)) {
            actividades.add(unaActividad)

        } else {
            throw BusinessException("No se puede agregar la actividad porque el horario esta ocupado")
        }
        actividades.sortBy { it.horarioInicio }
    }

    fun cantidadDeActidades() = actividades.size

    fun costoDeTotalDeActividades() = actividades.sumOf { it.costo }

    fun actividadesDeUnTipo(unaDificultad: Dificultad) = actividades.filter { it.dificultad == unaDificultad }


    fun tengoActividades() = actividades.isNotEmpty()

}

data class Actividad(
    var costo: Double,
    var descripcion: String,
    var horarioInicio: LocalTime,
    var horarioFin: LocalTime,
    var dificultad: Dificultad
) {
    fun duracion() = ChronoUnit.MINUTES.between(horarioInicio, horarioFin).toInt()

    fun validarDificultad() = (this.dificultad >= Dificultad.BAJA) && (this.dificultad <= Dificultad.ALTA)

    fun tieneInformacionCargadaEnDescripcion() = !this.descripcion.isNullOrEmpty()

    fun validarDuracion() = this.duracion() >= 0

    fun validarCosto() = costo >= 0

    fun validar() {
        if (!this.validarDificultad() || !this.tieneInformacionCargadaEnDescripcion() || !this.validarCosto() || !this.validarDuracion()) {
            throw FaltaCargarInformacionException("No se puede crear esta Actividad porque falta informacion o hay datos invalidos\n" +
            "dificultad: $dificultad, descripcion: $descripcion, duracion: ${duracion()}, costo: $costo")
        }
    }

}