package ar.edu.unsam.algo.TestUsuarios


import ar.edu.unsam.algo.Destino
import ar.edu.unsam.algo.Usuario
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate


class TestBelu : DescribeSpec({
        isolationMode = IsolationMode.InstancePerTest

        describe("Tests Destinos") {
            describe("Dado un destino no local") {
                val usuario1 = Usuario ("Juan", "Perez", "jperez", LocalDate.of(2021,4,13), "Francia",3)
                val usuario2 = Usuario("Marilu", "Cabak", "mcabak", LocalDate.of(2000,2,13), "Chile",3)
                val usuario3 = Usuario("Marilu", "Cabak", "mcabak", LocalDate.of(2000,11,3), "Brasil",3)
                val destino = Destino("Brasil", "San Pablo", 50000F)
                it("calcular el costo del viaje teniendo menos de 15 años de antiguedad") {
                    destino.esLocal() shouldBe false
                    destino.precio(usuario1) shouldBe 60000
                }
                it("calcular el costo del viaje teniendo más de 15 años de antiguedad") {
                    destino.esLocal() shouldBe false
                    destino.precio(usuario2) shouldBe 60000
                }
                it("calcular el costo del viaje siendo del mismo país del destino") {
                    destino.esLocal() shouldBe false
                    destino.precio(usuario3) shouldBe 52500
                }
            }
            describe("Dado un destino local"){
                val usuario4 = Usuario("Pamela", "Sosa", "psosa", LocalDate.of(2020,1,30), "Argentina",3)
                val usuario5 = Usuario("Gerardo", "Lopez", "jlopez", LocalDate.of(1993,3,13), "Argentina",3)
                val destino2 = Destino("Argentina", "Buenos Aires", 20000F)
                it("calcular el costo del viaje teniendo menos de 15 años de antiguedad"){
                    destino2.esLocal() shouldBe true
                    destino2.precio(usuario4) shouldBe 19600
                }
                it("calcular el costo del viaje teniendo más de 15 años de antiguedad"){
                    destino2.esLocal() shouldBe true
                    destino2.precio(usuario5) shouldBe 17000
                }
            }
        }
    })