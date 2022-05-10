package ar.edu.unsam.algo.TestUsuarios

import ar.edu.unsam.algo.Destino
import ar.edu.unsam.algo.Usuario
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestMios: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test Generales") {
        val usuario1 = Usuario(
            apellido = "Rodrigues",
            username = "El_Perri",
            nombre = "Sebas",
            fechaDeAlta = LocalDate.now().minusYears(16),
            paisDeResidencia = "Argentina",
            diasParaViajar = 4
        )
        val usuario2 = Usuario(
            apellido = "Martinez",
            username = "Holu204",
            nombre = "Camilo",
            fechaDeAlta = LocalDate.now().minusYears(3),
            paisDeResidencia = "Brasil",
            diasParaViajar = 4
        )
        describe("Usuario con Antiguedad y Local") {
            val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 3000F)
            describe("destino local") {
                destino1.esLocal() shouldBe true
                destino1.precio(usuario1) shouldBe 2550.0
            }
        }
        describe("Usuario con Poca Antiguedad y no Local") {
            val destino2 = Destino(pais = "Colombia", ciudad = "Bogota", costoBase = 25000F)
            describe("Destino No Local") {
                destino2.esLocal() shouldBe false
                destino2.precio(usuario2) shouldBe 30000.0
            }
        }
    }

})