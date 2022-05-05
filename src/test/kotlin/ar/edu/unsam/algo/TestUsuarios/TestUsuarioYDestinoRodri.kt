package ar.edu.unsam.algo.TestUsuarios


import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import java.time.LocalDate

class TestUsuarioYDestinoRodri: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de costos") {
        val usuario1 = Usuario(
            apellido = "Torres",
            username = "T20",
            nombre = "Nicolas",
            fechaDeAlta = LocalDate.of(2010, 1, 15),
            paisDeResidencia = "Alemania",
            diasParaViajar = 4
        )
        describe("Usuario con poca antiguedad no local") {
            val destino1 = Destino(pais = "Francia", ciudad = "Paris", costoBase = 20000F)
            describe("destino no local, se paga un extra por el pasaje") {
                destino1.esLocal() shouldBe false
                destino1.precio(usuario1) shouldBe 24000

            }
        }
        val usuario2 = Usuario(
            apellido = "Martinez",
            username = "Martu50",
            nombre = "Martin",
            fechaDeAlta = LocalDate.of(2005, 5, 7),
            paisDeResidencia = "Argentina",
            diasParaViajar = 4
        )
        describe("Usuario local con mucha antiguedad") {
            val destino2 = Destino(pais = "Argentina", ciudad = "Buenos aires", costoBase = 10000F)
            destino2.esLocal() shouldBe true
            destino2.precio(usuario2) shouldBe 8500
        }

    }
})