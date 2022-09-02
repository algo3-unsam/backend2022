package ar.edu.unsam.algo.TestItinerarios

import ar.edu.unsam.algo.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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
                var destinoIncompleto = Destino("", "", 0F)
                assertThrows<Exception> { destinoIncompleto.validacion() }
            }
            it("Creo un Destino Invalido porque su costo es menor a 0"){
                var destinoSinCosto = Destino("Argentina", "Buenos Aires",-7F)
                assertThrows<Exception> { destinoSinCosto.validacion() }
            }
            it("Creo un Destino valido") {
                var destinoValido = Destino("Argentina", "Mendoza", 5000f)
                assertDoesNotThrow { destinoValido.validacion() }
            }
        }
        describe("Test de Destinos Locales"){
            it("Testeo un destino en el pais local"){
                var destinoLocal = Destino("Argentina","Mendoza", 10000F)
                destinoLocal.esLocal().shouldBeTrue()
            }
            it("Testeo un destino que no es local"){
                var destinoNoLocal = Destino("Brasil","Sao Pablo", 15000F)
                destinoNoLocal.esLocal().shouldBeFalse()
            }
        }
    }
})

class TestDeItinerarios:DescribeSpec ({

    isolationMode = IsolationMode.InstancePerTest
    describe("Creo un itinerario ") {
        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3).apply{criterioParaItinerario = Relajado }
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val actividad = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
        val actividad2 = Actividad(150.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.BAJA)
        val actividad3 = Actividad(300.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.BAJA)
        val actividad4 = Actividad(350.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.MEDIA)
        val itinerarioConDificultadBaja = Itinerario(pepe, destino1, )
        val dia1 = Dia()
        val dia2 = Dia()
        val dia3 = Dia()
        val dia4 = Dia()
        itinerarioConDificultadBaja.apply {
            ocuparDia(dia1)
            ocuparDia(dia2)
            ocuparDia(dia3)
            ocuparDia(dia4)
            agregarActividadAlDia(dia1, actividad)
            agregarActividadAlDia(dia2, actividad2)
            agregarActividadAlDia(dia3, actividad3)
            agregarActividadAlDia(dia4, actividad4)
        }
        var itinerarioConDificultadAlta = Itinerario(pepe, destino1)
        val lunes = Dia()
        val martes = Dia()
        val miercoles = Dia()
        itinerarioConDificultadAlta.apply {
            ocuparDia(lunes)
            ocuparDia(martes)
            ocuparDia(miercoles)
            agregarActividadAlDia(lunes, actividad)
            agregarActividadAlDia(martes, actividad2)
            agregarActividadAlDia(miercoles, actividad4)
        }
        describe("Creo un itinerario invalido por falta de dias"){
            var itinerarioInvalido = Itinerario(pepe,destino1)
            it("Test de itinerario invalido por falta de dias iniciados"){
                assertThrows<FaltaCargarInformacionException> { itinerarioInvalido.validacion() }
            }
            var dia = Dia()
            var dia2 = Dia()
            itinerarioInvalido.apply{ocuparDia(dia); ocuparDia(dia2)}
            it("Test de itinerario invalido porque ningun dia tiene actividades"){
                assertThrows<FaltaCargarInformacionException> { itinerarioInvalido.validacion() }
            }
        }

        it("Testeo la validez de un itinerario valido"){
            assertDoesNotThrow { itinerarioConDificultadBaja.validacion() }
        }
        it("Test de Costo De Itinerarios") {
            itinerarioConDificultadBaja.totalCosto(pepe) shouldBe 3540.0
        }

        it("Test de Dificultad del Itinerario. Al tener ser dificultad BAJA la que mas actividades tiene, el itinerario tendra esa dificultad") {
            itinerarioConDificultadBaja.dificultad() shouldBe Dificultad.BAJA
        }

        it("Test de dificultad cuando todas las dificultades tengan la misma cantidad de actividades. La dificultad final sera la mas alta"){
            itinerarioConDificultadAlta.dificultad() shouldBe Dificultad.ALTA
        }
        it("prueba de no se agrega actividad"){
            val actividad5 = Actividad(150.0, "Hola!", LocalTime.of(9,0), LocalTime.of(10,0), Dificultad.BAJA)
            val actividad6 = Actividad(300.0, "Hola!", LocalTime.of(10,0), LocalTime.of(12,0), Dificultad.BAJA)
            val actividad7 = Actividad(350.0, "Hola!", LocalTime.of(10,30), LocalTime.of(11,30), Dificultad.MEDIA)

            val jueves = Dia()

            itinerarioConDificultadAlta.ocuparDia(jueves)

            itinerarioConDificultadAlta.agregarActividadAlDia(jueves, actividad5)
            itinerarioConDificultadAlta.agregarActividadAlDia(jueves, actividad6)
            shouldThrow<BusinessException> { itinerarioConDificultadAlta.agregarActividadAlDia(jueves, actividad7) }

            val actividad8 = Actividad(350.0, "Hola!", LocalTime.of(10,30), LocalTime.of(11,0), Dificultad.MEDIA)

            shouldThrow<BusinessException> {itinerarioConDificultadAlta.agregarActividadAlDia(jueves, actividad8) }

            val actividad9 = Actividad(350.0, "Hola!", LocalTime.of(9,0), LocalTime.of(12,30), Dificultad.MEDIA)
            shouldThrow<BusinessException> {itinerarioConDificultadAlta.agregarActividadAlDia(jueves, actividad9) }
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
                Dificultad.BAJA
            )
            assertThrows<Exception> { actividadConMalHorario.validar() }
        }
        it("Testeo una Actividad invalidad por costo menor a 0"){
            var actividadSinCosto = Actividad(-500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),
                Dificultad.BAJA
            )
            assertThrows<Exception> { actividadSinCosto.validar() }
        }
        var actividadValida = Actividad(500.0,"Visita al Museo",LocalTime.of(8,30),LocalTime.of(10,0),
            Dificultad.BAJA
        )
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
    var actividadConDificultadMedia = Actividad(300.0,"Circuito Chico",LocalTime.of(8,30),LocalTime.of(10,0), Dificultad.MEDIA)
    var actividadConDificultadAlta = Actividad(400.0,"Parapente",LocalTime.of(10,30),LocalTime.of(12,0), Dificultad.ALTA)
    dia.apply {
        agregarActividad(actividadConDificultadMedia)
        agregarActividad(actividadConDificultadAlta)
    }
    describe("Test de ingreso de actividad"){
        it("prueba de no se agrega actividad"){
            val actividad5 = Actividad(150.0, "Hola!", LocalTime.of(9,0), LocalTime.of(10,0), Dificultad.BAJA)

            assertThrows<Exception> { dia.agregarActividad(actividad5) }
        }
        it("Ingreso un actividad valida"){
            var actividadValida = Actividad(400.0,"Circuito Lagos", LocalTime.of(13,0),LocalTime.of(14,0),
                Dificultad.MEDIA
            )
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
                assertThrows<FaltaCargarInformacionException> {  usuarioIncompleto.validacion()  }
            }
            it("Testeo un usuario invalido por fecha del futuro"){
                var usuarioDelFuturo = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().plusYears(2),"Estados Unidos",7)
                usuarioDelFuturo.destinosDeseados.add(newYork)
                assertThrows<FaltaCargarInformacionException> {  usuarioDelFuturo.validacion()  }
            }
            it("Testeo un usuario invalido por dias para viajar menor a 0"){
                var usuarioSinViaje = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().minusYears(2),"Estados Unidos",-7)
                usuarioSinViaje.destinosDeseados.add(newYork)
                assertThrows<FaltaCargarInformacionException> {  usuarioSinViaje.validacion()  }
            }
            it("Testeo un usuario invalido por falta de Destino deseado"){
                var usuarioSinDeseo = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().minusYears(2),"Estados Unidos",7)
                assertThrows<FaltaCargarInformacionException> { usuarioSinDeseo.validacion() }
            }
            it("Testo un usuario valido"){
                var usuarioValido = Usuario("Martin","Mcfly","Mmcfly",LocalDate.now().minusYears(2),"Estados Unidos",7)
                usuarioValido.destinosDeseados.add(newYork)
                assertDoesNotThrow { usuarioValido.esValido() }
            }
        }
        describe("Testeo capacidad de realizar un itinerario segun criterio"){
            var usuario = Usuario("Leandro", "Amarilla", "LeaAmarilla", LocalDate.of(2012, 1, 18), "Argentina", 5)
            var tokio = Destino("Japon", "Tokio", 70000F)
            var itinerario1 = Itinerario(usuario, tokio)
            describe("Testeo un usuario relajado") {
                var dia = Dia()
                var dia2 = Dia()
                var dia3 = Dia()
                var dia4 = Dia()
                itinerario1.apply { ocuparDia(dia);ocuparDia(dia2);ocuparDia(dia3);ocuparDia(dia4) }
                usuario.cambiarCriterio(Relajado)
                it("Testeo que el usuario No puede realizar este itinerarios por no tener suficiente dias para viajar") {
                    usuario.diasParaViajar = 3
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeFalse()
                }
                it("Testeo que el usuario ahora puede realziar el itinerario") {
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
                it("Testeo cambio de criterio: con el nuevo criterio no puede realizar itinerario"){
                    usuario.cambiarCriterio(Localista)
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeFalse()
                }
            }
            describe("Testeo un usuario precavido"){
                usuario.cambiarCriterio(Precavido(usuario))
                it("El usuario no puede realizar el itinerario por no conocerlo anteriormente"){
                    usuario.puedeRealizarItinerario(itinerario1) shouldBe  false
                }
                it("El usuario puede realizar el itinerario porque ya lo concoce"){
                    usuario.destinosVisitados.add(tokio)
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
                it("El usuario puede realizar el itinerario porque un amigo suyo lo conoce"){
                    var amigo = Usuario("Juan","Perez","JpErez",LocalDate.of(2012,1,17),"Argentina",5)
                    amigo.destinosVisitados.add(tokio)
                    usuario.agregarAmigo(amigo)
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
            }
            describe("Testeo un usuario localista"){
                usuario.cambiarCriterio(Localista)
                it("No puede realizar el itinerario porque no es un destino local"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeFalse()
                }
                var destinoLocal = Destino("Argentina", "Mendoza", 3000F)
                itinerario1.destino = destinoLocal
                it("Testeo que si puede realizar este itinerario"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
            }
            describe("Testeo un usuario soñador"){
                usuario.cambiarCriterio(Soniador(usuario))
                var destinoSoñado = Destino("Qatar","Lusai",80000F)
                usuario.destinosDeseados.add(destinoSoñado)
                it("Testeo que no puede realizar el itineario por no tenerlo en destino Soñados y no ser mas caro que el destino mas caro"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeFalse()
                }
                usuario.destinosDeseados.add(tokio)
                it("Testeo que ahora puede realizar el itinerario por soñar el destino"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
                var destinoCaro = Destino("Alemania", "Berlin", 1000000F)
                var itinerarioCaro = Itinerario(usuario,destinoCaro)
                it("Testeo que puede realizar el itinerario por ser mas caro que sus soñados"){
                    usuario.puedeRealizarItinerario(itinerarioCaro).shouldBeTrue()
                }
            }
            describe("Testeo un usuario activo"){
                usuario.cambiarCriterio(Activo)
                var dia = Dia()
                var actividadConDificultadAlta = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
                dia.agregarActividad(actividadConDificultadAlta)
                itinerario1.ocuparDia(dia)
                var dia2 = Dia().apply { agregarActividad(actividadConDificultadAlta) }
                var dia3 = Dia().apply { agregarActividad(actividadConDificultadAlta) }
                var dia4 = Dia()
                itinerario1.apply {
                    ocuparDia(dia2)
                    ocuparDia(dia3)
                    ocuparDia(dia4)
                }
                it("No puede realizar itinerario porque no todos los dias tienen actividades"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeFalse()
                }
                itinerario1.agregarActividadAlDia(dia4,actividadConDificultadAlta)
                it("Ahora puede realizar itinerario porque todos los dias tienen actividades"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
            }
            describe("Testeo un usuario exigente"){
                usuario.cambiarCriterio(Exigente(Dificultad.MEDIA,40))
                var actividadConDificultadAlta = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
                var actividadConDificultadMedia = Actividad(100.0, "Hola!", LocalTime.of(10,50), LocalTime.of(11,30), Dificultad.MEDIA)
                var otraActividadConDificultadMedia = Actividad(100.0, "Hola!", LocalTime.of(19,30), LocalTime.of(20,30), Dificultad.MEDIA)
                var dia1 = Dia().apply { agregarActividad(actividadConDificultadAlta); agregarActividad(actividadConDificultadMedia) }
                var dia2 = Dia().apply { agregarActividad(otraActividadConDificultadMedia) }
                itinerario1.apply { ocuparDia(dia1);ocuparDia(dia2)}
                it("El porcentaje preferido del usuario coincide con el porcentaje de activades de determinada dificultad deseada"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeTrue()
                }
                usuario.cambiarCriterio(Exigente(Dificultad.ALTA,40))
                it("El porcentaje que hay no es suficiente entonces no puede realizar porcentaje"){
                    usuario.puedeRealizarItinerario(itinerario1).shouldBeFalse()
                }

            }
        }
        describe("Testeo puntuar a un itinerario"){
            var usuario = Usuario("Leandro", "Amarilla", "LeaAmarilla", LocalDate.of(2012, 1, 18), "Argentina", 5)
            var tokio = Destino("Japon", "Tokio", 70000F)
            var itinerario = Itinerario(usuario, tokio)
            it("Testeo no puntuar por ser creador de itinearario"){
                assertThrows<BusinessException> { usuario.puntuar(itinerario,9)}
            }
            var otroUsuario = Usuario("Juan","Perez","JpErez",LocalDate.of(2012,1,17),"Argentina",5)
            itinerario.creador = otroUsuario
            it("Testeo no puntuar por no conocer el destino"){
                assertThrows<BusinessException> { usuario.puntuar(itinerario,5)}
            }
            usuario.destinosVisitados.add(itinerario.destino)
            it("Testeo que pueda puntuar"){
                assertDoesNotThrow {usuario.puntuar(itinerario,5)}
            }
            it("Testeo que no pueda puntuar por haber puntuado antes"){
                usuario.puntuar(itinerario,5)
                assertThrows<BusinessException> { usuario.puntuar(itinerario,5) }
            }
            it("Test de chequeo de puntuaje"){
                usuario.puntuar(itinerario,5)
                usuario.consultarPuntaje(itinerario) shouldBe 5
            }
            it("Testeo consultar puntaje donde no se haya puntuado"){
                assertThrows<BusinessException> { usuario.consultarPuntaje(itinerario) }
            }
        }
    }
})