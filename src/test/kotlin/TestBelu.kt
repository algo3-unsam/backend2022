import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe


class TestBelu : DescribeSpec({
        isolationMode = IsolationMode.InstancePerTest

        describe("Tests Destinos") {
            describe("Dado un destino no local") {
                val usuario1 = Usuario ("Juan", "Perez", "jperez", 2021, "Francia")
                val usuario2 = Usuario("Marilu", "Cabak", "mcabak", 2000, "Chile")
                val destino = Destino("Brasil", "San Pablo", 50000F)
                it("calcular el costo del viaje teniendo menos de 15 años de antiguedad") {
                    destino.esLocal() shouldBe false
                    destino.precio(usuario1) shouldBe 59500
                }
                it("calcular el costo del viaje teniendo más de 15 años de antiguedad") {
                    destino.esLocal() shouldBe false
                    destino.precio(usuario2) shouldBe 52500
                }
            }
            describe("Dado un destino local"){
                val usuario3 = Usuario("Pamela", "Sosa", "psosa", 2020, "Argentina")
                val usuario4 = Usuario("Gerardo", "Lopez", "jlopez", 1993, "Argentina")
                val destino2 = Destino("Argentina", Ciudad = "Buenos Aires", 20000F)
                it("calcular el costo del viaje teniendo menos de 15 años de antiguedad"){
                    destino2.esLocal() shouldBe true
                    destino2.precio(usuario3) shouldBe 19600
                }
                it("calcular el costo del viaje teniendo más de 15 años de antiguedad"){
                    destino2.esLocal() shouldBe true
                    destino2.precio(usuario4) shouldBe 17000
                }
            }
        }
    })