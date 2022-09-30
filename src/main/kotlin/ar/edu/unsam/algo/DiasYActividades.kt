package ar.edu.unsam.algo

import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class Dificultad {
    BAJA,
    MEDIA,
    ALTA
}

class Dia(val actividades: MutableList<Actividad> = mutableListOf()) {

    fun agregarActividad(actividad: Actividad) {
        actividades.forEach{actividad.validarHorarioActividad(it)}
        actividades.add(actividad)
    }

    fun cantidadDeActidades() = actividades.size

    fun costoDeTotalDeActividades() = actividades.sumOf { it.costo }

    fun actividadesDeUnTipo(dificultad: Dificultad) = actividades.filter { it.dificultad == dificultad }

    fun tengoActividades() = actividades.isNotEmpty()
}

data class Actividad(
    var costo: Double,
    var descripcion: String,
    var horarioInicio: LocalTime,
    var horarioFin: LocalTime,
    var dificultad: Dificultad
) {
    fun coincidenciaConNombre(cadena:String) = descripcion.contains(cadena,ignoreCase = true)

    fun duracion() = ChronoUnit.MINUTES.between(horarioInicio, horarioFin).toInt()

    private fun tieneInformacionCargadaEnDescripcion() = this.descripcion.isNotEmpty()

    private fun validarDuracion() = this.duracion() >= 0

    private fun validarCosto() = costo >= 0

    fun validar() {
        if (!this.tieneInformacionCargadaEnDescripcion() || !this.validarCosto() || !this.validarDuracion()) {
            throw FaltaCargarInformacionException("No se puede crear esta Actividad porque falta informacion o hay datos invalidos\n" +
            "dificultad: $dificultad, descripcion: $descripcion, duracion: ${duracion()}, costo: $costo")
        }
    }

    fun validarHorarioActividad(actividad: Actividad) {
        if(!this.validarRangoInicioActividad(actividad) || !this.validarRangoFinActividad(actividad))
            throw BusinessException("No se puede agregar la actividad porque el horario de Inicio: ${this.horarioInicio} parecer estar ocupado")
    }

    private fun validarRangoFinActividad(actividad: Actividad) = actividad.horarioFin.between(this.horarioInicio, this.horarioFin)

    private fun validarRangoInicioActividad(actividad: Actividad) = actividad.horarioInicio.between(this.horarioInicio, this.horarioFin)
}