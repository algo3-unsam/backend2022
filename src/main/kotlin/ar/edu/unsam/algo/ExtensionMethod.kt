package ar.edu.unsam.algo

import java.time.LocalTime

fun LocalTime.between(horario: LocalTime) = horario >= this

fun dificultadConMasActividades(unItinerario: Itinerario,dificultadMasAlta: Dificultad,dificultadMasBaja:Dificultad):Dificultad{
    if(unItinerario.actividadesXDificultad()[dificultadMasBaja]!! > unItinerario.actividadesXDificultad()[dificultadMasAlta]!!){
        return  dificultadMasBaja
    }
    return dificultadMasAlta
}

fun rellenarDificultades(mapDeActividad: MutableMap<Dificultad,Int>,unaDificultad: Dificultad){
    if(!mapDeActividad.containsKey(unaDificultad)){
        mapDeActividad[unaDificultad] = 0
    }
}