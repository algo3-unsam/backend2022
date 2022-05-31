package ar.edu.unsam.algo.TestDeTareas

import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime


class TestViaje:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Verificar tareas"){
        val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
        val destino2 = Destino(pais = "Brasil", ciudad = "Rio", costoBase = 4000F)
        val destino3 = Destino(pais = "Alemania", ciudad = "Berlin", costoBase = 5000F)
        val destino4 = Destino(pais = "Turquia", ciudad = "Batman", costoBase = 6000F)
        val destino5 = Destino(pais = "Chile", ciudad = "Santiago", costoBase = 7000F)

        val pepe = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1), destinosDeseados = mutableListOf(destino2,destino4)).apply{criterioParaItinerario = Relajado}
        val pepe2 = Usuario("Juan", "Pelotas", "Pelotas01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1,destino3)).apply{criterioParaItinerario = Relajado }
        val marce = Usuario("Marce", "Lito", "Lito01", LocalDate.of(2010, 3, 12), "Argentina", diasParaViajar = 3, destinosVisitados = mutableListOf(destino1), amigos = mutableListOf(pepe,pepe2)).apply{criterioParaItinerario = Relajado }

        var moto200cc = Moto("honda","ninja",LocalDate.of(2015,7,5),10000.0,true,250)
        var motoSinConvenio = Moto("bmw","ninja",LocalDate.of(2015,7,5),10000.0,true,250)

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

        pepe.presupuesto = 40000.0
        pepe.activarAccion(AgregarAListaDeItinerariosParaPuntuar())
        pepe.activarAccion(RealizaViajeLocal())
        pepe.activarAccion(RealizaViajeConConvenio())
        pepe.realizar(viajeNoLocal)

        it("Verificar que la  lista de acciones tiene 3 acciones"){
            pepe.accionesActivas.size shouldBe 3
        }
        it("Verificar que si no es local cambiar criterio a Localista"){
            pepe.criterioParaItinerario shouldBe Localista
        }
        it("Verificar que agrego a itinerarios a puntar el itinerario del viaje"){
            pepe.puedoPuntuar(viajeNoLocal.itinerario) shouldBe true
        }
        it("Verificar que se agrega a lista de itinerarios, para puntuar todos juntos"){
            pepe.puntuarItinerarios(5)
            viajeNoLocal.itinerario.verPuntaje(pepe) shouldBe 5
        }
        describe("Test donde no activo acciones"){
            it("Verificar que si no me alcanza el presupuesto no realizo el viaje") {
                pepe2.presupuesto = 5000.0
                assertThrows<BusinessException> { pepe2.realizar(viajeNoLocal) }
                pepe2.destinosVisitados.shouldNotContain(viajeNoLocal.itinerario.destino)
            }

            pepe2.presupuesto = 40000.0
            pepe2.realizar(viajeNoLocal)

            it("Verificar que la lista de acciones este VACIA") {
                pepe2.accionesActivas.size shouldBe 0
            }
            it("Verificar que NO cambie a Localista") {
                pepe2.criterioParaItinerario shouldBe Relajado
            }
            it("Verificar que NO puedo puntuar") {
                pepe2.puedoPuntuar(viajeNoLocal.itinerario) shouldBe false
            }
            it("Verificar que NO puedo puntuar todos juntos") {
                assertThrows<BusinessException> { pepe2.puntuarItinerarios(5) }
            }
        }
        /*
        it("Verificar que si viaje no tiene vehiculo con convenio, cambia criterio a Selectivo "){
            pepe.criterioParaVehiculo shouldBe Selectivo("Honda")
        }*/

    }
})