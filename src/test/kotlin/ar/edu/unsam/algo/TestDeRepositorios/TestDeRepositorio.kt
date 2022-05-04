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
        var elementoParaAgregar = Destino("Argentina","Buenos Aires",10000F)
        var elementoEnRepoACambiar = Destino("Japon","Tokio",100000f)
        var elementoEnRepo = Destino("EEUU", "Nueva York", 130000f)
        repoDestinos.apply { create(elementoEnRepoACambiar); create(elementoEnRepo) }
        it("Testeo que el destino no esta en el repo"){
            repoDestinos.estaEnRepo(elementoParaAgregar).shouldBeFalse()
        }
        it("Testeo que destino ahora si se encuentra en el repositorio"){
            repoDestinos.create( elementoParaAgregar)
            repoDestinos.estaEnRepo(elementoParaAgregar).shouldBeTrue()
        }
        it("Testeo borrado del repo"){
            repoDestinos.create( elementoParaAgregar)
            repoDestinos.delete(elementoParaAgregar)
            repoDestinos.estaEnRepo(elementoParaAgregar).shouldBeFalse()
        }
        it("Intentar crear un destino que ya estaba creado tira excepcion"){
          assertThrows<BusinessException> { repoDestinos.create(elementoEnRepoACambiar) }
        }

        it("Testeo que el modificado este en el repo"){
            var destinoModificado = Destino("Japon","Tokio",50000f)
            destinoModificado.id = elementoEnRepoACambiar.id
            repoDestinos.update(destinoModificado)
            repoDestinos.estaEnRepo(destinoModificado) shouldBe true
        }
        it("Testeo la funcion search"){
            repoDestinos.create(elementoParaAgregar)
            var argelia = Destino("Argelia", "Argel",20000F)
            repoDestinos.create(argelia)
            var listaDeRespuesta = mutableListOf<Destino>().apply { add(elementoParaAgregar); add(argelia) }
            repoDestinos.search("Arg") shouldBe listaDeRespuesta
        }
    }
})

