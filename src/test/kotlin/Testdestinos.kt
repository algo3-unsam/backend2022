import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Period

class Testdestinos: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    val usuario1 = Usuario(Apellido = "Torres", Username = "T20", Nombre = "Nicolas", fechaDeAlta = "2010-01-15", paisDeResidencia = "Alemania")
    describe("Usuario con poca antiguedad no local"){
        val destino1 = Destino(Pais = "Francia", Ciudad = "Paris", costoBase = 20000F)
        describe("destino no local, se paga un extra por el pasaje"){
            destino1.esLocal() shouldBe false
            destino1.precio(usuario1) shouldBe 24000

        }
    }
    val usuario2 = Usuario(Apellido = "Martinez", Username = "Martu50", Nombre = "Martin", fechaDeAlta = "2005-05-07", paisDeResidencia = "Argentina")
    describe("Usuario local con mucha antiguedad"){
        val destino2 = Destino(Pais = "Argentina", Ciudad = "Buenos aires", costoBase = 10000F)
        destino2.esLocal() shouldBe true
        destino2.precio(usuario2) shouldBe 8500
    }

})