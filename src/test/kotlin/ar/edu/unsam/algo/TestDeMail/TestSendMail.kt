package ar.edu.unsam.algo.TestDeMail

import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalTime

class TestSendMail : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("dado un mail") {
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val destino2 = Destino(pais = "Brasil", ciudad = "Rio", costoBase = 4000F)
        val destino3 = Destino(pais = "Alemania", ciudad = "Berlin", costoBase = 5000F)
        val destino4 = Destino(pais = "Turquia", ciudad = "Batman", costoBase = 6000F)

        val pepe = Usuario(
            "Pepe",
            "Pelotas",
            "Pelotas01",
            LocalDate.of(2010, 3, 12),
            "Argentina",
            diasParaViajar = 3,
            destinosVisitados = mutableListOf(destino1),
            destinosDeseados = mutableListOf(destino2, destino4)
        ).apply { criterioParaItinerario = Relajado }
        val pepe2 = Usuario(
            "Pepe2",
            "Pelotas",
            "Pelotas01",
            LocalDate.of(2010, 3, 12),
            "Argentina",
            diasParaViajar = 3,
            destinosVisitados = mutableListOf(destino1, destino3),
            destinosDeseados = mutableListOf(destino2, destino4)
        ).apply { criterioParaItinerario = Relajado }
        val marce = Usuario(
            "Marce",
            "Lito",
            "Lito01",
            LocalDate.of(2010, 3, 12),
            "Argentina",
            diasParaViajar = 3,
            destinosVisitados = mutableListOf(destino1),
            amigos = mutableSetOf(pepe, pepe2)
        ).apply { criterioParaItinerario = Relajado }


        val motoSinConvenio = Moto("bmw", "ninja", LocalDate.of(2015, 7, 5), 10000.0, true, 250)

        val actividad = Actividad(100.0, "Hola!", LocalTime.of(9, 30), LocalTime.of(10, 30), Dificultad.ALTA)
        val actividad2 = Actividad(150.0, "Hola!", LocalTime.of(9, 30), LocalTime.of(10, 30), Dificultad.BAJA)
        val actividad3 = Actividad(300.0, "Hola!", LocalTime.of(9, 30), LocalTime.of(10, 30), Dificultad.BAJA)
        val actividad4 = Actividad(350.0, "Hola!", LocalTime.of(9, 30), LocalTime.of(10, 30), Dificultad.MEDIA)
        val itinerarioConDificultadBaja = Itinerario(marce, destino1)
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

        pepe.direccionDeCorreo = "pepe@gmail.com"
        pepe2.direccionDeCorreo = "pepe2@gmail.com"
        marce.direccionDeCorreo = "marce@gmail.com"

        it("Dado un mail debe mandarse"){
            val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
            val mail = Mail("app@holamundo.com", "${pepe.direccionDeCorreo}, ${pepe2.direccionDeCorreo}, ${marce.direccionDeCorreo}", "probando un test", "Lo que dice el asunto")
            mockedMailSender.sendMail(mail)
            verify(exactly = 1){
                mockedMailSender.sendMail(
                    mail = Mail(
                        from = "app@holamundo.com",
                        to = "${pepe.direccionDeCorreo}, ${pepe2.direccionDeCorreo}, ${marce.direccionDeCorreo}",
                        subject = "probando un test",
                        content = "Lo que dice el asunto"
                    )
                )
            }
        }

        it("se mandara mail a todos los amigos del usuario que realizo el viaje y desean el destino") {
            val tareaMandarMail = MandarMailAAmigosQueDeseanDestino()
            tareaMandarMail.mailSender = StubMailSender

            StubMailSender.reset()
            tareaMandarMail.realizaViaje(marce, viajeNoLocal)
            StubMailSender.mailsEnviados.size.shouldBe(2)
        }

    }
})



