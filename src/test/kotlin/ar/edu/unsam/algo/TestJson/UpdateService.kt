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
<<<<<<< HEAD
                "costo": 10000f
=======
                "costo": 10000
>>>>>>> 1e6478837ed03423e7e593ab492fd25e6e8fff25
            }, 
          {
                "pais": "Brasil",
                "ciudad": "Rio de Janeiro",
<<<<<<< HEAD
                "costo": 20000f
=======
                "costo": 20000
>>>>>>> 1e6478837ed03423e7e593ab492fd25e6e8fff25
             },
           {
                "id": 3,
                "pais": "Tailandia",
                "ciudad": "Bankog",
<<<<<<< HEAD
                "costo": 30000f
=======
                "costo": 30000
>>>>>>> 1e6478837ed03423e7e593ab492fd25e6e8fff25
           },
          {
                "pais": "Corea",
                "ciudad": "Seul",
<<<<<<< HEAD
                "costo": 54000f
=======
                "costo": 54000
>>>>>>> 1e6478837ed03423e7e593ab492fd25e6e8fff25
           }
        ]
    """.trimIndent()

    override fun getDestinos(): String {
        return destinosJSON
    }
}