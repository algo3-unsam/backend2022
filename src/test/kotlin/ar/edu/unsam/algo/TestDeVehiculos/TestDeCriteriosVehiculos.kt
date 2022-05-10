package ar.edu.unsam.algo.TestDeVehiculos

import ar.edu.unsam.algo.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate


class TestDeCriteriosVehiculos:DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest
    var usuario = Usuario("Leandro", "Amarilla", "LeaAmarilla", LocalDate.of(2012, 1, 18), "Argentina", 5)
    describe("Testeo un usuario Neofilo"){
        usuario.criterioParaVehiculo= Neofilo
        var motoVieja = Moto("Honda","Ninja", LocalDate.of(1999,2,1),1000.0,true,250)
        var motoNueva = Moto("Honda","Ninja", LocalDate.now().minusYears(1),1000.0,true,250)
        it("Usuario no acepta Moto Vieja"){
            usuario.leGustaVehiculo(motoVieja).shouldBeFalse()
        }
        it("Usuario le gusta Moto Nueva"){
            usuario.leGustaVehiculo(motoNueva).shouldBeTrue()
        }
    }
    describe("Testeo un usuario Superticioso"){
        usuario.criterioParaVehiculo = Supersticioso
        var autoPar = Auto("Audi", "TT", LocalDate.of(2010,5,12),100000.0,true,true)
        var autoImpar = Auto("Audi", "TT", LocalDate.of(2011,5,12),100000.0,true,true)
        it("Usuario no acepta auto con año de fabricacion impar"){
            usuario.leGustaVehiculo(autoImpar).shouldBeFalse()
        }
        it("Usuario le gusta auto con año de fabricacion par"){
            usuario.leGustaVehiculo(autoPar).shouldBeTrue()
        }
    }
    describe("Testeo de usuario Caprichoso"){
        usuario.criterioParaVehiculo = Caprichoso
        var autoInicialesDiferentes = Auto("Audi", "TT", LocalDate.of(2010,5,12),100000.0,true,true)
        var autoInicialesIguales = Auto("Ford", "Falcon", LocalDate.of(1979,5,12),100000.0,true,true)
        it("Usuario no acepta auto con marca y modelo de diferentes iniciales"){
            usuario.leGustaVehiculo(autoInicialesDiferentes).shouldBeFalse()
        }
        it("Usuario le gusta auto con marca y modelo con misma inicial"){
            usuario.leGustaVehiculo(autoInicialesIguales).shouldBeTrue()
        }
    }
    describe("Testeo de usuario Selectivo"){
        usuario.criterioParaVehiculo = Selectivo("Ferrari")
        var autoMarcaCorrecta = Auto("Ferrari", "LaFerrari", LocalDate.of(2010,5,12),100000.0,true,true)
        var autoMarcaIncorrecta = Auto("Audi", "TT", LocalDate.of(2011,5,12),100000.0,true,true)
        it("Usuario no acepta auto de otra marca"){
            usuario.leGustaVehiculo(autoMarcaIncorrecta).shouldBeFalse()
        }
        it("Usuario le gusta auto de marca preferida"){
            usuario.leGustaVehiculo(autoMarcaCorrecta).shouldBeTrue()
        }
    }
    describe("Testeo de usuario Sin limites"){
        usuario.criterioParaVehiculo = Supersticioso
        var camionetaLibre = Camioneta("Ford", "Eco Sport", LocalDate.of(2010,5,12),100000.0,true,true)
        var camionetaLimitada = Camioneta("Toyota", "Hilux", LocalDate.of(2011,5,12),100000.0,false,true)
        it("Usuario no acepta auto sin kilometraje libre"){
            usuario.leGustaVehiculo(camionetaLimitada).shouldBeFalse()
        }
        it("Usuario le gusta auto con kilometraje libre"){
            usuario.leGustaVehiculo(camionetaLibre).shouldBeTrue()
        }
    }
    describe("Testo de usuario combinado"){
        usuario.criterioParaVehiculo = Combinado().apply {agregarTipo(SinLimite);agregarTipo(Supersticioso)}
        var motoImpar = Moto("Honda","Ninja", LocalDate.of(1999,2,1),1000.0,true,250)
        var motoPar = Moto("Honda","Ninja", LocalDate.of(2000,2,1),1000.0,true,250)
        it("Usuario no acepta vehiculo. Si bien tiene kilometraje libre, el año de fabricacion es impar") {
            usuario.leGustaVehiculo(motoImpar).shouldBeFalse()
        }
        it("Usuario acepta vehiculo") {
            usuario.leGustaVehiculo(motoPar).shouldBeTrue()
        }
        it("Con otro tipo agregado, ahora no acepta motoPar que antes Si aceptaba") {
                (usuario.criterioParaVehiculo as Combinado).agregarTipo(Caprichoso)
                usuario.leGustaVehiculo(motoPar).shouldBeFalse()
        }
        it("Usuario acepta el vehiculo, teniendo como criterio un combinado con otro combinado dentro") {
            var subCombinado = Combinado().apply { agregarTipo(Selectivo("Ford")); agregarTipo(SinLimite) }
            (usuario.criterioParaVehiculo as Combinado).agregarTipo(subCombinado)
            var autoAceptable = Auto("Ford", "Fiesta", LocalDate.of(2022, 1, 21), 10000.0, true, true)
            usuario.leGustaVehiculo(autoAceptable).shouldBeTrue()

        }
    }
})