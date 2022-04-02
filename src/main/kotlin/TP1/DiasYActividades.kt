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
        return actividades.any {this.activdadActualTerminaATiempo(it,unaActividad) and this.hayEspacioParaNuevaActividad(it,unaActividad)  }
    }

    fun esUltimaActividad(unaActividad: Actividad) = (actividades.indexOf(unaActividad)+1) == actividades.size

    fun hayEspacioParaNuevaActividad(unaActividad: Actividad, nuevaActividad: Actividad) = (this.esUltimaActividad(unaActividad)) or (this.actividadSiguienteEmpiezaATiempo(unaActividad,nuevaActividad))

    fun activdadActualTerminaATiempo(unaActividad: Actividad,nuevaActividad: Actividad) = (unaActividad.fin <= nuevaActividad.inicio)

    fun actividadSiguienteEmpiezaATiempo(unaActividad: Actividad, nuevaActividad: Actividad): Boolean{
        var index= actividades.indexOf(unaActividad)
        var actividadSiguiente = actividades.get(index+1)
        return actividadSiguiente.inicio >= nuevaActividad.fin
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
        actividades.sortBy { it.inicio }
    }
}

data class Actividad(var costo: Double, var descrpcion: String, var inicio: LocalTime, var fin: LocalTime, var dificultad: Int) {
    fun duracion() = ChronoUnit.MINUTES.between(inicio,fin).toInt()

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