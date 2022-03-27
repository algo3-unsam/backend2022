package TP0

import Destino
import Usuario
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestDeRodri: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
<<<<<<< HEAD:src/test/kotlin/TestDeRodri.kt

    val usuario1 = Usuario(apellido = "Torres", username = "T20", nombre = "Nicolas", fechaDeAlta = LocalDate.of(2010,1,15), paisDeResidencia = "Alemania")

=======
    val usuario1 = Usuario(
        apellido = "Torres",
        username = "T20",
        nombre = "Nicolas",
        fechaDeAlta = LocalDate.of(2010, 1, 15),
        paisDeResidencia = "Alemania"
    )
>>>>>>> develop:src/test/kotlin/TP0/TestDeRodri.kt
    describe("Usuario con poca antiguedad no local"){
        val destino1 = Destino(pais = "Francia", ciudad = "Paris", costoBase = 20000F)
        describe("destino no local, se paga un extra por el pasaje"){
            destino1.esLocal() shouldBe false
            destino1.precio(usuario1) shouldBe 24000

        }
    }
<<<<<<< HEAD:src/test/kotlin/TestDeRodri.kt

    val usuario2 = Usuario(apellido = "Martinez", username = "Martu50", nombre = "Martin", fechaDeAlta = LocalDate.of(2005,5,7), paisDeResidencia = "Argentina")

=======
    val usuario2 = Usuario(
        apellido = "Martinez",
        username = "Martu50",
        nombre = "Martin",
        fechaDeAlta = LocalDate.of(2005, 5, 7),
        paisDeResidencia = "Argentina"
    )
>>>>>>> develop:src/test/kotlin/TP0/TestDeRodri.kt
    describe("Usuario local con mucha antiguedad"){
        val destino2 = Destino(pais = "Argentina", ciudad = "Buenos aires", costoBase = 10000F)
        destino2.esLocal() shouldBe true
        destino2.precio(usuario2) shouldBe 8500
    }

})