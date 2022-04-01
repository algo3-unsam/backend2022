package TP1
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime


class TestDeItinerarios:DescribeSpec ({
    describe("Creo un itinerario ") {
        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3).apply{criterio = Relajado()}
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val actividad = Actividad(100.0, "Hola!", LocalDateTime.of(2022,2,12,9,30), LocalDateTime.of(2022,2,12,10,30), ALTA)
        val actividad2 = Actividad(150.0, "Hola!", LocalDateTime.of(2022,2,12,9,30), LocalDateTime.of(2022,2,12,10,30), BAJA)
        val actividad3 = Actividad(300.0, "Hola!", LocalDateTime.of(2022,2,12,9,30), LocalDateTime.of(2022,2,12,10,30), BAJA)
        val actividad4 = Actividad(350.0, "Hola!", LocalDateTime.of(2022,2,12,9,30), LocalDateTime.of(2022,2,12,10,30), MEDIA)
        val unItinerario = Itinerario(pepe, destino1, 5)
        val dia1 = Dia()
        val dia2 = Dia()
        val dia3 = Dia()
        val dia4 = Dia()
        unItinerario.apply {
            ocuparDia(dia1)
            ocuparDia(dia2)
            ocuparDia(dia3)
            ocuparDia(dia4)
            agregarActividad(dia1, actividad)
            agregarActividad(dia2, actividad2)
            agregarActividad(dia3, actividad3)
            agregarActividad(dia4, actividad4)
        }
        it("Test de Costo De Itinerarios") {
            unItinerario.totalCosto() shouldBe 900.0
        }

        it("Test de Dificultad del Itinerario. Al tener ser dificultad BAJA la que mas actividades tiene, el itinerario tendra esa dificultad") {
            unItinerario.dificultad() shouldBe BAJA
        }
        var otroItinerario = Itinerario(pepe, destino1, 5)
        val lunes = Dia()
        val martes = Dia()
        val miercoles = Dia()
        otroItinerario.apply {
            ocuparDia(lunes)
            ocuparDia(martes)
            ocuparDia(miercoles)
            agregarActividad(lunes, actividad)
            agregarActividad(martes, actividad2)
            agregarActividad(miercoles, actividad4)
        }
        it("Test de dificultad cuando todas las dificultades tengan la misma cantidad de actividades. La dificultad final sera la mas alta"){
            otroItinerario.dificultad() shouldBe ALTA
        }
    }

})