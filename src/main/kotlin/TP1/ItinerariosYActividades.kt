package TP1

val ALTA = 3
val MEDIA  = 2
val BAJA = 1


class Itinerario(var creador: Usuario, var destino: Destino, var cantDias: Int, var actividades: MutableList<Actividad> = mutableListOf()){
    fun totalCosto() = actividades.sumOf{ it.costo }

    fun agregarActividad(unaActividad: Actividad){
        actividades.add(unaActividad)
    }

    fun sosMiCreador(unUsuario: Usuario) = (unUsuario.username == creador.username )

    fun dificultad(): Int {
        var actividadAlta = actividades.filter { it.dificultad == ALTA}.size
        var actividadMedia = actividades.filter { it.dificultad == MEDIA}.size
        var actividadBaja = actividades.filter { it.dificultad == BAJA}.size
        if (actividadAlta >= actividadMedia){
           if(actividadAlta >= actividadBaja){
               return ALTA
           }
           else
               return BAJA
        }
        else if(actividadMedia>=actividadBaja){
            return MEDIA
        }
        return BAJA
    }
}


data class Actividad(var costo: Double, var descrpcion: String, var inicio: Int, var fin: Int, var dificultad : Int){
    fun duracion() = fin - inicio
}