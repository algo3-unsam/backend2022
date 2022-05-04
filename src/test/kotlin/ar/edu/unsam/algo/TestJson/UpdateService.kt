package ar.edu.unsam.algo

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.lang.reflect.Type


class UpdateService: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    describe("Test de Json"){
        val repoDestino = Repositorio<Destino>()
        var destino1 = Destino("Brasil","Rio de Janeiro",5000f)
        var destino2 = Destino("Argentina","Mar Del Plata",4000f)
        var destino3 = Destino("Peru","Lima",8000f)
        val actualizadorDeRepo = ActualizadorDeDestinos()
        repoDestino.apply { create(destino1);create(destino2);create(destino3) }
        it("El repo tiene 3 elementos"){
            repoDestino.cantElementos() shouldBe 3
        }
        actualizadorDeRepo.serviceDestino = StubServiceDestino()
        actualizadorDeRepo.repositorio = repoDestino
        actualizadorDeRepo.actualizarDestinos()
        it("El repo tiene 4 elementos"){
            repoDestino.cantElementos() shouldBe 5
        }
        it("Modifico bien el segundo destino"){
            var destinoModificado = repoDestino.getById(2)
            destinoModificado.ciudad shouldBe "Buenos Aires"
        }

    }
})






class StubServiceDestino : ServiceDestino{
    val destinosJSON ="""
        [
           {
                "id": 2,
                "pais": "Argentina",
                "ciudad": "Buenos Aires",
                "costo": 10000f
            }, 
          {
                "pais": "Brasil",
                "ciudad": "Rio de Janeiro",
                "costo": 20000f
             },
           {
                "id": 3,
                "pais": "Tailandia",
                "ciudad": "Bankog",
                "costo": 30000f
           },
          {
                "pais": "Corea",
                "ciudad": "Seul",
                "costo": 54000f
           }
        ]
    """.trimIndent()

    override fun getDestinos(): String {
        return destinosJSON
    }
}