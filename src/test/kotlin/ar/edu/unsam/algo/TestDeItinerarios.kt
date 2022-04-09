package ar.edu.unsam.algo


import io.kotest.assertions.throwables.shouldThrow

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
        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3).apply{criterio = Relajado }
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        describe("Creo un itinerario invalido por falta de dias"){
            var itinerarioInvalido = Itinerario(pepe,destino1,4)
            it("Test de itinerario invalido por falta de dias iniciados"){
                assertThrows<FaltaCargarInformacion> { itinerarioInvalido.validar() }
            }
            var dia =Dia()
            var dia2 = Dia()
            itinerarioInvalido.apply{ocuparDia(dia); ocuparDia(dia2)}
            it("Test de itinerario invalido porque ningun dia tiene actividades"){
                assertThrows<FaltaCargarInformacion> { itinerarioInvalido.validar() }
            }
        }
        val actividad = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
        val actividad2 = Actividad(150.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.BAJA)
        val actividad3 = Actividad(300.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.BAJA)
        val actividad4 = Actividad(350.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.MEDIA)
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
        it("Testeo la validez de un itinerario valido"){
            assertDoesNotThrow { unItinerario.validar() }
        }
        it("Test de Costo De Itinerarios") {
            unItinerario.totalCosto() shouldBe 900.0
        }

        it("Test de Dificultad del Itinerario. Al tener ser dificultad BAJA la que mas actividades tiene, el itinerario tendra esa dificultad") {
            unItinerario.dificultad() shouldBe Dificultad.BAJA
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
            otroItinerario.dificultad() shouldBe Dificultad.ALTA
        }
        it("prueba de no se agrega actividad"){
            val actividad5 = Actividad(150.0, "Hola!", LocalTime.of(9,0), LocalTime.of(10,0), Dificultad.BAJA)
            val actividad6 = Actividad(300.0, "Hola!", LocalTime.of(10,0), LocalTime.of(12,0), Dificultad.BAJA)
            val actividad7 = Actividad(350.0, "Hola!", LocalTime.of(10,30), LocalTime.of(11,30), Dificultad.MEDIA)

            val jueves = Dia()

            otroItinerario.ocuparDia(jueves)

            otroItinerario.agregarActividad(jueves, actividad5)
            otroItinerario.agregarActividad(jueves, actividad6)
            shouldThrow<FaltaCargarInformacion> { otroItinerario.agregarActividad(jueves, actividad7) }

            val actividad8 = Actividad(350.0, "Hola!", LocalTime.of(10,30), LocalTime.of(11,0), Dificultad.MEDIA)

            shouldThrow<FaltaCargarInformacion> {otroItinerario.agregarActividad(jueves, actividad8) }

            val actividad9 = Actividad(350.0, "Hola!", LocalTime.of(9,0), LocalTime.of(12,30), Dificultad.MEDIA)
            shouldThrow<FaltaCargarInformacion> {otroItinerario.agregarActividad(jueves, actividad9) }
        }
    }
})

class TestDeActividades:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de validez de Actividades"){
        it("Testeo una Actividad invalida por falta de parametros"){
            var actividadIncompleta = Actividad(0.0,"",LocalTime.of(9,30),LocalTime.of(11,0), Dificultad.ALTA)
            assertThrows<Exception> { actividadIncompleta.validar() }
        }
        it("Testeo una Activida invalida por error en hora de inicio y final"){
            var actividadConMalHorario = Actividad(0.0,"Visita al Museo",LocalTime.of(10,0),LocalTime.of(8,30),
                Dificultad.BAJA)
            assertThrows<Exception> { actividadConMalHorario.validar() }
        }
        it("Testeo una Actividad invalidad por costo menor a 0"){
            var actividadSinCosto = Actividad(-500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),
                Dificultad.BAJA)
            assertThrows<Exception> { actividadSinCosto.validar() }
        }
        var actividadValida = Actividad(500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),
            Dificultad.BAJA)
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
    var primerActividad = Actividad(300.0,"Circuito Chico",LocalTime.of(8,30),LocalTime.of(10,0), Dificultad.MEDIA)
    var segundaActividad = Actividad(400.0,"Parapente",LocalTime.of(10,30),LocalTime.of(12,0), Dificultad.ALTA)
    dia.apply {
        agregarActividadAlDia(primerActividad)
        agregarActividadAlDia(segundaActividad)
    }
    describe("Test de ingreso de actividad"){
        it("prueba de no se agrega actividad"){
            val actividad5 = Actividad(150.0, "Hola!", LocalTime.of(9,0), LocalTime.of(10,0), Dificultad.BAJA)

            assertThrows<Exception> { dia.agregarActividadAlDia(actividad5) }
        }
        it("Ingreso un actividad valida"){
            var actividadValida = Actividad(400.0,"Circuito Lagos", LocalTime.of(13,0),LocalTime.of(14,0),
                Dificultad.MEDIA)
            assertDoesNotThrow { dia.agregarActividadAlDia(actividadValida) }
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
                assertThrows<FechaInvalida> {  usuarioDelFuturo.esValido()  }
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
        describe("Testeo capacidad de realizar un itinerario segun criterio"){
            var unUsuario = Usuario("Leandro", "Amarilla", "LeaAmarilla", LocalDate.of(2012, 1, 18), "Argentina", 5)
            var tokio = Destino("Japon", "Tokio", 70000F)
            var unItinerario = Itinerario(unUsuario, tokio, 4)
            describe("Testeo un usuario relajado") {
                unUsuario.cambiarCriterio(Relajado)
                it("Testeo que el usuario No puede realizar este itinerarios por no tener suficiente dias para viajar") {
                    unUsuario.diasParaViajar = 3
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }
                it("Testeo que el usuario ahora puede realziar el itinerario") {
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
                it("Testeo cambio de criterio: con el nuevo criterio no puede realizar itinerario"){
                    unUsuario.cambiarCriterio(Localista)
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }
            }
            describe("Testeo un usuario precavido"){
                unUsuario.cambiarCriterio(Precavido(unUsuario))
                it("El usuario no puede realizar el itinerario por no conocerlo anteriormente"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe  false
                }
                it("El usuario puede realizar el itinerario porque ya lo concoce"){
                    unUsuario.destinosVisitados.add(tokio)
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
                it("El usuario puede realizar el itinerario porque un amigo suyo lo conoce"){
                    var amigo = Usuario("Juan","Perez","JpErez",LocalDate.of(2012,1,17),"Argentina",5)
                    amigo.destinosVisitados.add(tokio)
                    unUsuario.agregarAmigo(amigo)
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
            }
            describe("Testeo un usuario localista"){
                unUsuario.cambiarCriterio(Localista)
                it("No puede realizar el itinerario porque no es un destino local"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }
                var destinoLocal = Destino("Argentina", "Mendoza", 3000F)
                unItinerario.destino = destinoLocal
                it("Testeo que si puede realizar este itinerario"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
            }
            describe("Testeo un usuario soñador"){
                unUsuario.cambiarCriterio(Soniador(unUsuario))
                var unDestinoSoñado = Destino("Qatar","Lusai",80000F)
                unUsuario.destinosDeseados.add(unDestinoSoñado)
                it("Testeo que no puede realizar el itineario por no tenerlo en destino Soñados y no ser mas caro que el destino mas caro"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }
                unUsuario.destinosDeseados.add(tokio)
                it("Testeo que ahora puede realizar el itinerario por soñar el destino"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
                var unDestinoCaro = Destino("Alemania", "Berlin", 1000000F)
                var itinerarioCaro = Itinerario(unUsuario,unDestinoCaro,4)
                it("Testeo que puede realizar el itinerario por ser mas caro que sus soñados"){
                    unUsuario.puedeRealizarItinerario(itinerarioCaro) shouldBe true
                }
            }
            describe("Testeo un usuario activo"){
                unUsuario.cambiarCriterio(Activo)
                var dia = Dia()
                var unaActividad = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
                dia.agregarActividadAlDia(unaActividad)
                unItinerario.ocuparDia(dia)
                it("No puede realizar porque no tiene todos los dias ocupados"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }
                var dia2 = Dia().apply { agregarActividadAlDia(unaActividad) }
                var dia3 = Dia().apply { agregarActividadAlDia(unaActividad) }
                var dia4 = Dia()
                unItinerario.apply {
                    ocuparDia(dia2)
                    ocuparDia(dia3)
                    ocuparDia(dia4)
                }
                it("No puede realizar itinerario porque no todos los dias tienen actividades"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }
                unItinerario.agregarActividad(dia4,unaActividad)
                it("Ahora puede realizar itinerario porque todos los dias tienen actividades"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
            }
            describe("Testeo un usuario exigente"){
                unUsuario.cambiarCriterio(Exigente(Dificultad.MEDIA,40))
                var unaActividadAlta = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
                var unaActividadMedia = Actividad(100.0, "Hola!", LocalTime.of(10,50), LocalTime.of(11,30), Dificultad.MEDIA)
                var otraActividadMedia = Actividad(100.0, "Hola!", LocalTime.of(19,30), LocalTime.of(20,30), Dificultad.MEDIA)
                var dia1 = Dia().apply { agregarActividadAlDia(unaActividadAlta); agregarActividadAlDia(unaActividadMedia) }
                var dia2 = Dia().apply { agregarActividadAlDia(otraActividadMedia) }
                unItinerario.apply { ocuparDia(dia1);ocuparDia(dia2)}
                it("El porcentaje preferido del usuario coincide con el porcentaje de activades de determinada dificultad deseada"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe true
                }
                unUsuario.cambiarCriterio(Exigente(Dificultad.ALTA,40))
                it("El porcentaje que hay no es suficiente entonces no puede realizar porcentaje"){
                    unUsuario.puedeRealizarItinerario(unItinerario) shouldBe false
                }

            }
        }
        describe("Testeo puntuar a un itinerario"){
            var unUsuario = Usuario("Leandro", "Amarilla", "LeaAmarilla", LocalDate.of(2012, 1, 18), "Argentina", 5)
            var tokio = Destino("Japon", "Tokio", 70000F)
            var unItinerario = Itinerario(unUsuario, tokio, 4)
            it("Testeo no puntuar por ser creador de itinearario"){
                assertThrows<Exception> { unUsuario.puntuar(unItinerario,9)}
            }
            var otroUsuario = Usuario("Juan","Perez","JpErez",LocalDate.of(2012,1,17),"Argentina",5)
            unItinerario.creador = otroUsuario
            it("Testeo no puntuar por no conocer el destino"){
                assertThrows<Exception> { unUsuario.puntuar(unItinerario,5)}
            }
            unUsuario.destinosVisitados.add(unItinerario.destino)
            it("Testeo que pueda puntuar"){
                assertDoesNotThrow {unUsuario.puntuar(unItinerario,5)}
            }
            it("Testeo que no pueda puntuar por haber puntuado antes"){
                unUsuario.puntuar(unItinerario,5)
                assertThrows<Exception> { unUsuario.puntuar(unItinerario,5) }
            }
            it("Test de chequeo de puntuaje"){
                unUsuario.puntuar(unItinerario,5)
                unUsuario.consultarPuntaje(unItinerario) shouldBe 5
            }
            it("Testeo consultar puntaje donde no se haya puntuado"){
                assertThrows<Exception> { unUsuario.consultarPuntaje(unItinerario) }
            }
        }
    }
})