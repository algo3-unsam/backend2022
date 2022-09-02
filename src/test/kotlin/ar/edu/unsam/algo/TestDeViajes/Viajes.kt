package ar.edu.unsam.algo.TestDeViajes

import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime


class TestViaje:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Verificar tareas"){
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val destino2 = Destino(pais = "Brasil", ciudad = "Rio", costoBase = 4000F)
        val destino3 = Destino(pais = "Alemania", ciudad = "Berlin", costoBase = 5000F)
        val destino4 = Destino(pais = "Turquia", ciudad = "Batman", costoBase = 6000F)

        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1), destinosDeseados = mutableListOf(destino2,destino4)).apply{criterioParaItinerario = Relajado}
        val pepe2 = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1,destino3)).apply{criterioParaItinerario = Relajado;criterioParaVehiculo=SinLimite }
        val marce = Usuario("Marce", "Lito", "Lito01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1),destinosDeseados = mutableListOf(destino2), amigos = mutableSetOf(pepe,pepe2)).apply{criterioParaItinerario = Relajado }

        val moto200cc = Moto("honda","ninja",LocalDate.of(2015,7,5),10000.0,true,250)
        val motoSinConvenio = Moto("bmw","ninja",LocalDate.of(2015,7,5),10000.0,true,250)

        val actividad = Actividad(100.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.ALTA)
        val actividad2 = Actividad(150.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.BAJA)
        val actividad3 = Actividad(300.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.BAJA)
        val actividad4 = Actividad(350.0, "Hola!", LocalTime.of(9,30), LocalTime.of(10,30), Dificultad.MEDIA)
        val itinerarioConDificultadBaja = Itinerario(marce, destino1, )
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
        val itinerarioConDificultadAlta = Itinerario(marce, destino2)
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

        val viajeNoLocal = Viaje(vehiculo = motoSinConvenio, itinerario = itinerarioConDificultadAlta)

        val repo = RepositorioDeItinerarios()
        repo.agregarAlRepo(itinerarioConDificultadBaja)
        pepe.agregarAmigo(marce)
        pepe.agregarAmigo(pepe2)

        val tareaMandarMail = MandarMailAAmigosQueDeseanDestino()
        tareaMandarMail.mailSender = StubMailSender

        val accionDeViajeLocal = RealizaViajeLocal()

        describe("Test donde activo 4 acciones"){
            pepe.activarAccion(AgregarAListaDeItinerariosParaPuntuar())
            pepe.activarAccion(accionDeViajeLocal)
            pepe.activarAccion(accionDeViajeLocal)
            pepe.activarAccion(accionDeViajeLocal)
            pepe.activarAccion(accionDeViajeLocal)
            pepe.activarAccion(RealizaViajeConConvenio())

            pepe.activarAccion(tareaMandarMail)
            StubMailSender.reset()

            pepe.realizar(viajeNoLocal)

            it("Verificar que la  lista de acciones tiene 4 acciones") {
                pepe.observerDeViajesActivas.size shouldBe 4
            }
            it("Verifico que se manda mail a amigo que desea el destino del viaje y no al que no"){
                StubMailSender.mailsEnviados.size.shouldBe(1)
            }
            it("Verificar que si NO es local cambia criterio a Localista") {
                pepe.criterioParaItinerario shouldBe Localista
            }
            it("Verificar que se agrega a itinerarios a puntuar el itinerario del viaje"){
                pepe.listaItinerariosParaPuntuar shouldBe mutableSetOf(itinerarioConDificultadAlta)
            }
            it("Verificar que se cambia el criterio de vehiculo a Selectivo"){
                pepe.leGustaVehiculo(motoSinConvenio) shouldBe false
                pepe.leGustaVehiculo(moto200cc) shouldBe true
            }

        }
        describe("Test donde no activo acciones"){

            StubMailSender.reset()
            pepe2.realizar(viajeNoLocal)
            it("Verificar que la  lista de acciones tiene 4 acciones") {
                pepe2.observerDeViajesActivas.size shouldBe 0
            }

            it("Verifico que NO se manda mail a amigo que desea el destino"){
                StubMailSender.mailsEnviados.size.shouldBe(0)
            }

            it("Verificar que NO cambie a Localista") {
                pepe2.criterioParaItinerario shouldBe Relajado
            }
            it("Verificar que NO agrego a itinerarios a puntar el itinerario del viaje"){
                repo.filtrarPorPuntuables(pepe2) shouldBe mutableListOf(itinerarioConDificultadBaja)
            }
            it("Verificar que NO se cambia el criterio de vehiculo a Selectivo"){
                pepe2.leGustaVehiculo(motoSinConvenio) shouldBe true
                pepe2.leGustaVehiculo(moto200cc) shouldBe true
            }

        }

    }
})