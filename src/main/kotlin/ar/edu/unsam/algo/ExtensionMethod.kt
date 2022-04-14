package ar.edu.unsam.algo

import java.time.LocalTime

fun LocalTime.between(inicio: LocalTime, fin: LocalTime) =  (this <= inicio) && (this <= fin)

