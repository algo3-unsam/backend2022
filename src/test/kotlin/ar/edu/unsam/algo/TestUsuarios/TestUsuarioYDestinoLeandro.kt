package ar.edu.unsam.algo.TestUsuarios

import ar.edu.unsam.algo.Destino
import ar.edu.unsam.algo.Usuario
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class TestDePasajes: DescribeSpec( {
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de Costo de Destino para diferentes usuarios"){
        val usuarioArg = Usuario("Juan", "Perez", "JuanP", LocalDate.of(2003,3,22), "Argentina",diasParaViajar = 4)
        it("El usuario tiene excede el maximo de antiguedad pero recibe el maximo descuento que se puede otorgar"){
            usuarioArg.descuentoPorAntiguedad() shouldBe  15
        }
        it("El usuario va a un destino local, por lo tanto recibe descuento por antiguedad y no recibe aumento"){
            val cordoba = Destino("Argentina", "Cordoba", 20000F)
            cordoba.precio(usuarioArg) shouldBe 17000F
        }
        it("El usuario viaja al exterior, recibe el aumento pero no el descuento"){
            val rio = Destino("Brasil", "Rio de Janeiro", 10000F)
            rio.precio(usuarioArg) shouldBe 12000F

        }
        describe("Test de usuario extranjero"){
            val usuarioYankee = Usuario ("Kevin", "Durant", "KD2017", LocalDate.now().minusYears(12), "Estados Unidos", diasParaViajar = 4)
            it("El usuario recibira un 12% de descuento por antiguedad"){
                usuarioYankee.descuentoPorAntiguedad() shouldBe 12
            }
            it("El usuario va a un destino local, no recibe aumento pero tampoco descuento"){
                val bsAs = Destino("Argentina", "Buenos Aires", 15000F)
                bsAs.precio(usuarioYankee) shouldBe  15000F
            }
            it("El usuario viaja a su pais de residencia, recibe aumento y descuento"){
                val miami = Destino("Estados Unidos", "Miami", 10000F)
                miami.precio(usuarioYankee) shouldBe  10800F
            }
            it("El usuario viaja al exterior, pero no a Estados Unidos, recibe aumento pero no descuento"){
                val roma = Destino("Italia", "Roma", 10000F)
                roma.precio(usuarioYankee) shouldBe 12000F
            }
        }
    }
})

