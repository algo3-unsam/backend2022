package TP0

import Destino
import Usuario
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestDeRodri: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest
    val usuario1 = Usuario(
        apellido = "Torres",
        username = "T20",
        nombre = "Nicolas",
        fechaDeAlta = LocalDate.of(2010, 1, 15),
        paisDeResidencia = "Alemania"
    )
    describe("Usuario con poca antiguedad no local"){
        val destino1 = Destino(pais = "Francia", ciudad = "Paris", costoBase = 20000F)
        describe("destino no local, se paga un extra por el pasaje"){
            destino1.esLocal() shouldBe false
            destino1.precio(usuario1) shouldBe 24000

        }
    }
    val usuario2 = Usuario(
        apellido = "Martinez",
        username = "Martu50",
        nombre = "Martin",
        fechaDeAlta = LocalDate.of(2005, 5, 7),
        paisDeResidencia = "Argentina"
    )
    describe("Usuario local con mucha antiguedad"){
        val destino2 = Destino(pais = "Argentina", ciudad = "Buenos aires", costoBase = 10000F)
        destino2.esLocal() shouldBe true
        destino2.precio(usuario2) shouldBe 8500
    }

})