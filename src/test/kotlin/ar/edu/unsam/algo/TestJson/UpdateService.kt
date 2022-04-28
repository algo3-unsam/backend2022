package ar.edu.unsam.algo.TestJson

import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UpdateService: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de Json"){
        var json = Json()
        var destino1 = Destino("Argentina","Mendoza", 10000F)
        var destino2 = Destino("Brasil","Rio de Janeiro", 20000F)
        destino2.id = 1
        json.agregarDestino(destino1)
        json.agregarDestino(destino2)
        it("Contiene los destinos"){
            json.contieneDestinos(destino1) shouldBe true
            json.contieneDestinos(destino2) shouldBe true
        }
        println(json.destinoJson)
    }})