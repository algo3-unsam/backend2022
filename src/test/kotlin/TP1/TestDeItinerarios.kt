package TP1

import ALTA
import Actividad
import BAJA
import Destino
import Itinerario
import MEDIA
import Usuario
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestDeItinerarios:DescribeSpec ({
    describe("Creo un itinerario ") {
        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina")
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val actividad = Actividad(100.0, "Hola!", 9, 10, ALTA)
        val actividad2 = Actividad(150.0, "Hola!", 9, 10, BAJA)
        val actividad3 = Actividad(300.0, "Hola!", 9, 10, BAJA)
        val actividad4 = Actividad(350.0, "Hola!", 9, 10, MEDIA)
        val unItinerario = Itinerario(pepe, destino1, 5)
        unItinerario.agregarActividad(actividad)
        unItinerario.agregarActividad(actividad2)
        unItinerario.agregarActividad(actividad3)
        unItinerario.agregarActividad(actividad4)
        it("Test de Costo De Itinerarios") {
            unItinerario.totalCosto() shouldBe 900.0
        }
        it("Test de Dificultad del Itinerario. Al tener ser dificultad BAJA la que mas actividades tiene, el itinerario tendra esa dificultad") {
            unItinerario.dificultad() shouldBe BAJA
        }
        var otroItinerario = Itinerario(pepe, destino1, 5)
        otroItinerario.agregarActividad(actividad)
        otroItinerario.agregarActividad(actividad2)
        otroItinerario.agregarActividad(actividad4)
        it("Test de dificultad cuando todas las dificultades tengan la misma cantidad de actividades. La dificultad final sera la mas alta"){
            otroItinerario.dificultad() shouldBe ALTA
        }
    }

})