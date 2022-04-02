package TP1

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime

class TestDestinos:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de Destino") {
        describe("Test de Validez de Destino") {
            it("Creo un Destino invalido porque tiene vacias sus parametros") {
                var unDestinoIncompleto = Destino("", "", 0F)
               assertThrows<Exception> { unDestinoIncompleto.validar() }
            }
            it("Creo un Destino Invalido porque su costo es menor a 0"){
                var unDestinoSinCosto = Destino("Argentina", "Buenos Aires",-7F)
                assertThrows<Exception> { unDestinoSinCosto.validar() }
            }
            it("Creo un Destino valido") {
                var unDestinoValido = Destino("Argentina", "Mendoza", 5000f)
                assertDoesNotThrow { unDestinoValido.validar() }
            }
        }
        describe("Test de Destinos Locales"){
            it("Testeo un destino en el pais local"){
                var unDestinoLocal = Destino("Argentina","Mendoza", 10000F)
                unDestinoLocal.esLocal() shouldBe true
            }
            it("Testeo un destino que no es local"){
                var unDestinoNoLocal = Destino("Brasil","Sao Pablo", 15000F)
                unDestinoNoLocal.esLocal() shouldBe false
            }
        }
    }
})

class TestDeItinerarios:DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    describe("Creo un itinerario ") {
        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3).apply{criterio = Relajado()}
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val actividad = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultades.alta.numero)
        val actividad2 = Actividad(150.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultades.baja.numero)
        val actividad3 = Actividad(300.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultades.baja.numero)
        val actividad4 = Actividad(350.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultades.media.numero)
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
            unItinerario.dificultad() shouldBe Dificultades.baja.numero
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
            otroItinerario.dificultad() shouldBe Dificultades.alta.numero
        }
    }
})

class TestDeActividades:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de validez de Actividades"){
        it("Testeo una Actividad invalida por falta de parametros"){
            var actividadIncompleta = Actividad(0.0,"",LocalTime.of(9,30),LocalTime.of(11,0),Dificultades.alta.numero)
            assertThrows<Exception> { actividadIncompleta.validar() }
        }
        it("Testeo una Activida invalida por error en hora de inicio y final"){
            var actividadConMalHorario = Actividad(0.0,"Visita al Museo",LocalTime.of(10,0),LocalTime.of(8,30),Dificultades.baja.numero)
            assertThrows<Exception> { actividadConMalHorario.validar() }
        }
        it("Testeo una Actividad invalidad por costo menor a 0"){
            var actividadSinCosto = Actividad(-500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),Dificultades.baja.numero)
            assertThrows<Exception> { actividadSinCosto.validar() }
        }
        it("Testeo una actividad invalidad por dificultad invalida"){
            var actividadDificultadInvalida = Actividad(500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),4)
            assertThrows<Exception> { actividadDificultadInvalida.validar() }
        }
        it("Testeo una actividad invalidad por dificultad invalida"){
            var actividadDificultadInvalida = Actividad(500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),-4)
            assertThrows<Exception> { actividadDificultadInvalida.validar() }
        }
        var actividadValida = Actividad(500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),Dificultades.baja.numero)
        it("Testeo una Actividad valida"){
           assertDoesNotThrow { actividadValida.validar() }
        }
        it("Testeo duracion de Actividad"){
            actividadValida.duracion() shouldBe 90
        }
    }
})

class TestDias:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    var dia = Dia()
    var primerActividad = Actividad(300.0,"Circuito Chico",LocalTime.of(8,30),LocalTime.of(10,0),Dificultades.media.numero)
    var segundaActividad = Actividad(400.0,"Parapente",LocalTime.of(10,30),LocalTime.of(12,0),Dificultades.alta.numero)
    dia.apply {
        agregarActividad(primerActividad)
        agregarActividad(segundaActividad)
    }
    describe("Test de ingreso de actividad invalida"){
        it("Ingreso un actividad que empieza antes de que una actividad que ya estaba en el dia termine"){
            var actividadInvalida = Actividad(400.0,"Circuito Lagos", LocalTime.of(11,0),LocalTime.of(13,9),Dificultades.media.numero)
            assertThrows<Exception> {  dia.agregarActividad(actividadInvalida) }
        }
        it("Ingreso un actividad valida"){
            var actividadValida = Actividad(400.0,"Circuito Lagos", LocalTime.of(13,0),LocalTime.of(14,0),Dificultades.media.numero)
            assertDoesNotThrow { dia.agregarActividad(actividadValida) }
        }
    }
})