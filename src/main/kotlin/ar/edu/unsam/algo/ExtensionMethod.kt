package ar.edu.unsam.algo

import java.time.LocalTime

fun LocalTime.between(inicio: LocalTime, fin: LocalTime) =  (this <= inicio) && (this <= fin)

fun containsString(unaList: MutableList<String>, cadena : String): Boolean{
    return unaList.any { it.equals(cadena,ignoreCase = true) }
}
