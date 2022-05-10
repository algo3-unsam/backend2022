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
        repoDestino.apply { create(destino1);create(destino2);create(destino3) }
        ActualizadorDeDestinos.serviceDestino = StubServiceDestino()
        ActualizadorDeDestinos.repositorio = repoDestino
        it("Al actualizar el repo, se agregan mas destinos al repo"){
            repoDestino.cantElementos() shouldBe 3
            ActualizadorDeDestinos.actualizarDestinos()
            repoDestino.cantElementos() shouldBe 5
        }

        it("Al usar el actualziador de destinos, el elemento en la posicion 2 cambia de estado") {
            var destinoAModificar = repoDestino.getById(2)
            destinoAModificar.ciudad shouldBe "Mar Del Plata"

            ActualizadorDeDestinos.actualizarDestinos()

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