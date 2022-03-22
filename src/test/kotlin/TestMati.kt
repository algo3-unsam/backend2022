import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class TestMati : DescribeSpec({
    describe("Testeo descuentos de personas que viajan a su pais de residencia"){
        val usuario1 = Usuario(Apellido = "Gani", Username = "Mati", Nombre = "Matias", fechaDeAlta = "2002-03-20", paisDeResidencia = "Argentina")
        val usuario2 = Usuario(Apellido = "Javi", Username = "Mart", Nombre = "Martin", fechaDeAlta = "2012-03-20", paisDeResidencia = "Argentina")
        val destino1 = Destino(Pais = "Argentina", Ciudad = "BuenosAires", costoBase = 10000F)
        val destino2 = Destino(Pais = "Argentina", Ciudad = "Cordoba", costoBase = 20000F)

        it("Verificar que el descuento de una persona con 20 años de antiguedad es el 15%"){
            usuario1.descuentoPorAntiguedad() shouldBe 15
        }
        it("Verificar que el descuento de una persona con 20 años de antiguedad es el 15%"){
            destino1.descuento(usuario1) shouldBe 1500
        }
        it("Verificar que el descuento de una persona con 10 años de antiguedad es el 10%"){
            usuario1.descuentoPorAntiguedad() shouldBe 15
        }
        it("Verificar que el descuento de una persona con 10 años de antiguedad es el 10%"){
            destino2.descuento(usuario2) shouldBe 2000
        }
    }
    describe("Testeo descuentos de personas que NO viajan a su pais de residencia"){
        val usuario = Usuario(Apellido = "Martinez  ", Username = "Bryan", Nombre = "Bryan", fechaDeAlta = "2002-03-20", paisDeResidencia = "Brasil")
        val destino = Destino(Pais = "Argentina", Ciudad = "BuenosAires", costoBase = 10000F)
        it("Verificar que el descuento de una persona con 20 años de antiguedad es el 15%"){
            usuario.descuentoPorAntiguedad() shouldBe 15
        }
        it("Verificar que el descuento es 0"){
            destino.descuento(usuario) shouldBe 0
        }
    }

})