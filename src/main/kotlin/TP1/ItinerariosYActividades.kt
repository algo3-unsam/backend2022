package TP1

val ALTA = 3
val MEDIA  = 2
val BAJA = 1


class Dia(var actividades: MutableList<Actividad> = mutableListOf()){

}

class Itinerario(var creador: Usuario, var destino: Destino, var cantDias: Int, var actividades: MutableList<Actividad> = mutableListOf()){
    var dia: MutableList<actividades> = mutableListOf()


    fun totalCosto() = actividades.sumOf{ it.costo }

    fun agregarActividad(unaActividad: Actividad){
        actividades.add(unaActividad)
    }



    fun todoLosDiasOcupados() = cantDias == actividades.size

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

    fun porcentajeDeActividadXDificultad(unaDificultad: Int) = (actividades.filter{ it.dificultad == unaDificultad }.size * 100)/actividades.size


}


data class Actividad(var costo: Double, var descrpcion: String, var inicio: Int, var fin: Int, var dificultad : Int){
    fun duracion() = fin - inicio
}