package ar.edu.unsam.algo.TestDeTareas

import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime


class TestTareas:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Verificar tareas"){
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val destino2 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 4000F)
        val destino3 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 5000F)
        val destino4 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 6000F)
        val destino5 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 7000F)
        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1), destinosDeseados = mutableListOf(destino2,destino4)).apply{criterioParaItinerario = Relajado }
        val pepe2 = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1,destino3)).apply{criterioParaItinerario = Relajado }
        val marce = Usuario("Marce", "Lito", "Lito01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1), amigos = mutableSetOf(pepe,pepe2)).apply{criterioParaItinerario = Relajado }


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
        val itinerarioConDificultadAlta = Itinerario(marce, destino1)
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
        val repoDeUsuario = RepositorioDeUsuarios()
        repoDeUsuario.agregarAlRepo(pepe)
        repoDeUsuario.agregarAlRepo(pepe2)
        val repoDeItinerarios = RepositorioDeItinerarios()
        repoDeItinerarios.agregarAlRepo(itinerarioConDificultadBaja)
        repoDeItinerarios.agregarAlRepo(itinerarioConDificultadAlta)
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        it("Verificar que se puntuan correctamente"){

            pepe.listaItinerariosParaPuntuar.addAll(mutableSetOf(itinerarioConDificultadAlta, itinerarioConDificultadBaja))
            pepe.realizarTarea(PuntuarItinerarios(5, mockedMailSender))
            itinerarioConDificultadAlta.puntajes[pepe.username] shouldBe 5
            itinerarioConDificultadBaja.puntajes[pepe.username] shouldBe 5
        }

        it("Verificar que se transfieren itinerarios"){
            marce.realizarTarea(TranseferirItinerarios(repoDeItinerarios, mockedMailSender))
            pepe.esCreadorDe(itinerarioConDificultadAlta) shouldBe true
            pepe.esCreadorDe(itinerarioConDificultadBaja) shouldBe true
        }

        it("Verificar que se hace amigo del que conocen el destino y no del que no"){
            marce.amigos.clear()
            marce.realizarTarea(AgregarAmigos(repoDeUsuario,destino2, mockedMailSender))
            marce.amigos.size shouldBe 1
            marce.amigos.contains(pepe)
        }
        it("Verificar que se hace amigo de los que conocen el destino"){
            marce.amigos.clear()
            marce.realizarTarea(AgregarAmigos(repoDeUsuario,destino1, mockedMailSender))
            marce.amigos.size shouldBe 2
            marce.amigos.containsAll(mutableListOf(pepe,pepe2))
        }
        it("Verificar que se agregan destinos deseados mas caros"){
            pepe2.destinosDeseados.add(destino5)
            marce.realizarTarea(AgregarDeseadoCaroDeAmigos(mockedMailSender))
            marce.destinosDeseados shouldBe (mutableListOf(destino4,destino5))
        }
    }
})