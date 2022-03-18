import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class Testdestinos: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    val usuario1 = Usuario(Apellido = "Torres", Username = "T20", Nombre = "Nicolas", fechaDeAlta = 2010, paisDeResidencia = "Alemania")
    describe("Usuario con mucha antiguedad no local"){
        val destino1 = Destino(Pais = "Francia", Ciudad = "Paris", costoBase = 20000F)
        describe("destino no local, se paga un extra por el pasaje"){
            destino1.esLocal() shouldBe false
            destino1.precio(usuario1) shouldBe 21600
        }
    }

})