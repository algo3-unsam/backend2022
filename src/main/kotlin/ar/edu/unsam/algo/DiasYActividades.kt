package ar.edu.unsam.algo

import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class Dificultad() {
    BAJA,
    MEDIA,
    ALTA
}

class Dia(val actividades: MutableList<Actividad> = mutableListOf()) {

    fun agregarActividad(actividad: Actividad) {
        if (!actividades.all{actividad.validarHorarioActividad(it)}) {
            throw BusinessException("No se puede agregar la actividad porque el horario de Fin: ${actividad.horarioFin} parecer estar ocupado\"")
        }
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

    fun validarHorarioActividad(actividad: Actividad): Boolean {
        return if(!this.validarRangoInicioActividad(actividad)){
            throw BusinessException("No se puede agregar la actividad porque el horario de Inicio: ${this.horarioInicio} parecer estar ocupado")
        }else{
            this.validarRangoFinActividad(actividad)
        }
    }

    fun validarRangoFinActividad(actividad: Actividad) = actividad.horarioFin.between(this.horarioInicio, this.horarioFin)

    fun validarRangoInicioActividad(actividad: Actividad) = actividad.horarioInicio.between(this.horarioInicio, this.horarioFin)
}