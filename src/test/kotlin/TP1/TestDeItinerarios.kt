package TP1

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate


class TestDeItinerarios : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Creo un itinerario ") {
        val pepe = UsuarioRelajado("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3)
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val actividad = Actividad(100.0, "Hola!", 9, 10, ALTA)
        val actividad2 = Actividad(150.0, "Hola!", 9, 10, BAJA)
        val actividad3 = Actividad(300.0, "Hola!", 9, 10, BAJA)
        val actividad4 = Actividad(350.0, "Hola!", 9, 10, MEDIA)

        val unItinerario = Itinerario(pepe, destino1, 5)

        val primer_dia_itinerario1_pepe = Dia("Lunes")
        val segundo_dia_itinerario1_pepe = Dia("Miercoles")
        val tercer_dia_itinerario1_pepe = Dia("Jueves")

        unItinerario.ocuparDia(primer_dia_itinerario1_pepe)
        unItinerario.ocuparDia(segundo_dia_itinerario1_pepe)
        unItinerario.ocuparDia(tercer_dia_itinerario1_pepe)


        it("se agreagan actividades a determinado dia") {
            unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad)
            unItinerario.agregarActividad(segundo_dia_itinerario1_pepe, actividad3)
            unItinerario.agregarActividad(tercer_dia_itinerario1_pepe, actividad4)

            primer_dia_itinerario1_pepe.actividades.contains(actividad) shouldBe true
            segundo_dia_itinerario1_pepe.actividades.contains(actividad3) shouldBe true
            tercer_dia_itinerario1_pepe.actividades.contains(actividad4) shouldBe true
        }

        it("no se puede agregar una actividad con el mismo horario al mismo dia") {
            unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad)
            shouldThrow<Exception> { unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad2) }
        }

        it("Test de Costo De Itinerarios") {
            unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad)
            unItinerario.agregarActividad(segundo_dia_itinerario1_pepe, actividad3)
            unItinerario.agregarActividad(tercer_dia_itinerario1_pepe, actividad4)
            unItinerario.totalCosto() shouldBe 750.0
        }

        it("teniendo dos actividades bajas debe dar 2") {
            unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad)
            unItinerario.agregarActividad(segundo_dia_itinerario1_pepe, actividad2)
            unItinerario.agregarActividad(tercer_dia_itinerario1_pepe, actividad3)
            unItinerario.actividadesDifBaja() shouldBe 2
        }

        it("Test de Dificultad del Itinerario. Al ser dificultad BAJA la que mas actividades tiene, el itinerario tendra esa dificultad") {
            unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad)
            unItinerario.agregarActividad(segundo_dia_itinerario1_pepe, actividad2)
            unItinerario.agregarActividad(tercer_dia_itinerario1_pepe, actividad3)

            unItinerario.dificultad() shouldBe BAJA
        }

        it("Test de dificultad cuando todas las dificultades tengan la misma cantidad de actividades. La dificultad final sera la mas alta") {
            unItinerario.agregarActividad(primer_dia_itinerario1_pepe, actividad)
            unItinerario.agregarActividad(segundo_dia_itinerario1_pepe, actividad2)
            unItinerario.agregarActividad(tercer_dia_itinerario1_pepe, actividad4)

            unItinerario.dificultad() shouldBe ALTA
        }
    }

})