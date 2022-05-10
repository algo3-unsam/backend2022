package ar.edu.unsam.algo.TestUsuarios


import ar.edu.unsam.algo.Destino
import ar.edu.unsam.algo.Usuario
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestUsuarioYDestinoMati : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de costos por usuario") {
        describe("Testeo descuentos de personas que viajan a su pais de residencia") {
            val usuarioCon20AniosDeAnt = Usuario(
                apellido = "Gani",
                username = "Mati",
                nombre = "Matias",
                fechaDeAlta = LocalDate.now().minusYears(20),
                paisDeResidencia = "Argentina",
                diasParaViajar = 4
            )
            val usuarioCon10AniosDeAnt = Usuario(
                apellido = "Javi",
                username = "Mart",
                nombre = "Martin",
                fechaDeAlta = LocalDate.now().minusYears(10),
                paisDeResidencia = "Argentina",
                diasParaViajar = 4
            )
            val destino1 = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 10000F)
            val destino2 = Destino(pais = "Argentina", ciudad = "Cordoba", costoBase = 20000F)

            it("Verificar que el descuento de una persona con 20 años de antiguedad es el 15%") {
                usuarioCon20AniosDeAnt.descuentoPorAntiguedad() shouldBe 15
            }
            it("Verificar que se realizo el descuento correspondiente del 15%") {
                destino1.descuento(usuarioCon20AniosDeAnt) shouldBe 1500
            }
            it("Verificar que el descuento de una persona con 10 años de antiguedad es el 10%") {
                usuarioCon10AniosDeAnt.descuentoPorAntiguedad() shouldBe 10
            }
            it("Verificar que se realizo el descuento correspondiente del 10%") {
                destino2.descuento(usuarioCon10AniosDeAnt) shouldBe 2000
            }
        }
        describe("Testeo descuentos de personas que NO viajan a su pais de residencia") {
            val usuario = Usuario(
                apellido = "Martinez  ",
                username = "Bryan",
                nombre = "Bryan",
                fechaDeAlta = LocalDate.now().minusYears(20),
                paisDeResidencia = "Brasil",
                diasParaViajar = 4
            )
            val destino = Destino(pais = "Argentina", ciudad = "BuenosAires", costoBase = 10000F)
            it("Verificar que el descuento de una persona con 20 años de antiguedad es el 15%") {
                usuario.descuentoPorAntiguedad() shouldBe 15
            }
            it("Verificar que el descuento es 0 por no viajar a su pais de residencia") {
                destino.descuento(usuario) shouldBe 0
            }
        }
    }
    
})