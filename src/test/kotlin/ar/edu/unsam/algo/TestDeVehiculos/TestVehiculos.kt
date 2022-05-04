package ar.edu.unsam.algo.TestDeVehiculos


import ar.edu.unsam.algo.Auto
import ar.edu.unsam.algo.Camioneta
import ar.edu.unsam.algo.Moto
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestVehiculos:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de Vehiculo"){
        describe("Test de Moto"){
            var moto200cc = Moto("honda","ninja",LocalDate.of(2015,7,5),10000.0,true,250)
            var motoSinConvenio = Moto("bmw","ninja",LocalDate.of(2015,7,5),10000.0,true,250)
            var moto300ccYConConvenio = Moto("honda","ninja",LocalDate.of(2015,7,5),10000.0,true,300)
            it("Costo Final con Convenio de un alquiler de 3 dias"){
                moto200cc.costoFinal(3) shouldBe 27000
            }
            it("Costo Final sin Convenio de un alquiler de 3 dias"){
                motoSinConvenio.costoFinal(3) shouldBe 30000
            }
            it("Costo Final con Convenio de un alquiler de 3 dias con mas cilindrada"){
                moto300ccYConConvenio.costoFinal(3) shouldBe 28350
            }
        }
        describe("Test de Auto"){
            var autoConBaul = Auto("bmw","m3",LocalDate.of(2015,7,5),10000.0, true,false)
            var autoSinBaul = Auto("bmw","m10",LocalDate.of(2015,7,5),10000.0, true,true)
            it("Costo Final por Auto no Hatchback"){
                autoConBaul.costoFinal(3) shouldBe 37500
            }
            it("Costo Final por Auto Hatchback"){
                autoSinBaul.costoFinal(3) shouldBe 33000
            }
        }
        describe("Test de Camioneta"){
            var camionetaNormal = Camioneta("bmw","x3",LocalDate.of(2015,7,5),10000.0, true,false)
            var camioneta4x4 = Camioneta("bmw","x3",LocalDate.of(2015,7,5),10000.0,true, true)
            it("Test menos de una semana"){
                camionetaNormal.costoFinal(7) shouldBe 80000
            }
            it("Test mas de una semana Alquilando"){
                camionetaNormal.costoFinal(10) shouldBe 113000
            }

            it("Test de Camioneta 4x4"){
                camioneta4x4.costoFinal(10) shouldBe 163000
            }

        }
    }
})