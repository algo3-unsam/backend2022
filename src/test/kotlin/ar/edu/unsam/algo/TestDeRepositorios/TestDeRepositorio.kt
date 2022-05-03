package ar.edu.unsam.algo

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class TestDeRepositorio:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Testeo un Repositorio de Destinos"){
        var repoDestinos = Repositorio<Destino>()
        var bsAs = Destino("Argentina","Buenos Aires",10000F)
        it("Testeo que el destino no esta en el repo"){
            repoDestinos.estaEnRepo(bsAs).shouldBeFalse()
        }
        repoDestinos.create( bsAs)
        it("Testeo que destino ahora si se encuentra en el repositorio"){
            repoDestinos.estaEnRepo(bsAs).shouldBeTrue()
        }
        it("Testeo borrado del repo"){
            repoDestinos.delete(bsAs)
            repoDestinos.estaEnRepo(bsAs).shouldBeFalse()
        }
        var tokio = Destino("Japon","Tokio",100000f)
        var nuevaYork = Destino("EEUU", "Nueva York", 130000f)
        repoDestinos.apply { create(tokio); create(nuevaYork) }
        it("Intentar crear un destino que ya estaba creado tira excepcion"){
          assertThrows<BusinessException> { repoDestinos.create(tokio) }
        }
        var nuevoTokio = tokio
        nuevoTokio = Destino("Japon","Tokio",50000f)
        nuevoTokio.id = tokio.id
        repoDestinos.update(nuevoTokio)
        it("Testeo que el modificado este en el repo"){
            repoDestinos.estaEnRepo(nuevoTokio) shouldBe true
        }
        it("Testeo la funcion search"){
            var argelia = Destino("Argelia", "Argel",20000F)
            repoDestinos.create(argelia)
            var listaDeRespuesta = mutableListOf<Destino>().apply { add(bsAs); add(argelia) }
            repoDestinos.search("Arg") shouldBe listaDeRespuesta
        }
    }
})

