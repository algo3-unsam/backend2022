import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestMios: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    val usuario1 = Usuario(Apellido = "Rodrigues", Username = "El_Perri", Nombre = "Sebas", fechaDeAlta = "2005-07-20", paisDeResidencia = "Argentina")
    val usuario2 = Usuario(Apellido = "Martinez", Username = "Holu204", Nombre = "Camilo", fechaDeAlta = "2018-11-05", paisDeResidencia = "Brasil")
    describe("Usuario con Antiguedad y Local"){
        val destino1 = Destino(Pais = "Argentina", Ciudad = "BuenosAires", costoBase = 3000F)
        describe("destino local"){
            destino1.esLocal() shouldBe true
            destino1.precio(usuario1) shouldBe 2550.0
        }
    }
    describe("Usuario con Poca Antiguedad y no Local"){
        val destino2 = Destino(Pais = "Colombia", Ciudad = "Bogota", costoBase = 25000F)
        describe("Destino No Local"){
            destino2.esLocal() shouldBe false
            destino2.precio(usuario2) shouldBe 30000.0
        }
    }

})