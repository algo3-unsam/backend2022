package TP1


import io.kotest.assertions.throwables.shouldThrow

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeOneOf
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
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

        it("No se puede agregar una actividad a un determinado dia porque coincide con el horario de otra actividad"){
            shouldThrow<Exception> { unItinerario.agregarActividad(lunes, actividad2) }

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

class TestDeUsuarios:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de Usuarios"){
        describe("Test de validez de Usuarios"){
            var newYork = Destino("EEUU", "Nueva York",60000F)
            it("Testeo un usuario invalido por falta de parametros"){
                var usuarioIncompleto = Usuario("","","",LocalDate.of(2012,3,12),"",9)
                usuarioIncompleto.destinosDeseados.add(newYork)
                assertThrows<Exception> {  usuarioIncompleto.esValido()  }
            }
            it("Testeo un usuario invalido por fecha del futuro"){
                var usuarioDelFuturo = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().plusYears(2),"Estados Unidos",7)
                usuarioDelFuturo.destinosDeseados.add(newYork)
                assertThrows<Exception> {  usuarioDelFuturo.esValido()  }
            }
            it("Testeo un usuario invalido por dias para viajar menor a 0"){
                var usuarioSinViaje = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().minusYears(2),"Estados Unidos",-7)
                usuarioSinViaje.destinosDeseados.add(newYork)
                assertThrows<Exception> {  usuarioSinViaje.esValido()  }
            }
            it("Testeo un usuario invalido por falta de Destino deseado"){
                var usuarioSinDeseo = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().minusYears(2),"Estados Unidos",7)
                assertThrows<Exception> { usuarioSinDeseo.esValido() }
            }
            it("Testo un usuario valido"){
                var usuarioValido = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().minusYears(2),"Estados Unidos",7)
                usuarioValido.destinosDeseados.add(newYork)
                assertDoesNotThrow { usuarioValido.esValido() }
            }
        }
        describe("Testeo Cambio de criterio"){
            var unUsuario = Usuario("Leandro","Amarilla","LeaAmarilla",LocalDate.of(2012,1,18),"Argentina",10)
            unUsuario.criterio = Relajado()
            it("El criterio es Relajado"){
                unUsuario.criterio.nombre() shouldBe "Relajado"
            }
            unUsuario.cambiarCriterio(Localista())
            it("El criterio es Localista"){
                unUsuario.criterio.nombre() shouldBe "Localista"
            }
        }
        describe("Testeo capacidad de realizar un itinerario segun criterio"){
            var unUsuario = Usuario("Leandro","Amarilla","LeaAmarilla",LocalDate.of(2012,1,18),"Argentina",2)
            var tokio = Destino("Japon","Tokio",70000F)
            var unItinerario = Itinerario(unUsuario,tokio,4)
            unUsuario.cambiarCriterio(Relajado())
            it("Testeo que el usuario No puede realizar este itinerarios por no tener suficiente dias para viajar"){
                unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
            }
        }
    }
})