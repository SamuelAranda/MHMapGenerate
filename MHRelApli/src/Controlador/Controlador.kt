package Controlador

import BBDD.DBManagerNuevoPedido
import Modelo.Celda
import Modelo.Material
import Modelo.Monstruo
import Modelo.Objeto
import kotlin.random.Random

class Controlador {
    val conn = DBManagerNuevoPedido()

    init {
        conn.connectToDataBase()
    }

    fun imprimeMonstruo(nombre: String): Monstruo{
        var  monstruo = Monstruo()
        monstruo = conn.devolverDatosTablaMonstruo(conn.devolverIdCualquiera(nombre, "Monstruo"), monstruo)
        return monstruo
    }

    fun imprimeMonstruo(id: Int): Monstruo{
        conn.connectToDataBase()
        var monstruo = Monstruo()
        monstruo = conn.devolverDatosTablaMonstruo(id, monstruo)

        return monstruo
    }

    fun imprimeMaterial(nombre: String): Material{
        var material = Material()
        material = conn.devolverDatosTablaMaterial(conn.devolverIdCualquiera(nombre, "Material"), material)
        material.objetos = conn.devolverDatosObjetoMaterial(conn.devolverIdCualquiera(nombre, "Material"))

        return material
    }

    fun imprimeMaterial(id: Int): Material{
        conn.connectToDataBase()
        var material = Material()
        material = conn.devolverDatosTablaMaterial(id, material)
        material.objetos = conn.devolverDatosObjetoMaterial(id)

        return material
    }

    fun imprimeObjeto(id: Int): Objeto{
        conn.connectToDataBase()
        var objeto = Objeto()
        objeto = conn.devolverDatosTablaObjeto(id, objeto)

        return objeto
    }

    fun imprimeObjeto(nombre: String): Objeto{
        conn.connectToDataBase()
        var objeto = Objeto()
        objeto = conn.devolverDatosTablaObjeto(conn.devolverIdCualquiera(nombre, "Objeto"), objeto)

        return objeto
    }

    fun imprimeCelda(int: Int): Celda{

        val celda = Celda()
        return conn.devolverDatosTablaCelda(int, celda)

    }

    fun generarEventoAleatorio(){
        var id = Random.nextInt(0, 28)

        conn.devolverDatosEvento(id)
    }

    fun generarEventoAleatorioTipoDado(tipoEvento: String){
        conn.devolverDatosEvento(tipoEvento)
    }

    fun generarIDCelda(): Int{

       return conn.devolverUltimoId("Celda")+1

    }

    fun crearCeldaEjemplo(tipoTerreno: String){
        var celda = Celda(tipoTerreno)

        celda.id = generarIDCelda()
        celda.objetos = generarListaObjetos(10)
        celda.material = generarListaMaterial(tipoTerreno)
        celda.evento = conn.devolverDatosEvento("Secretos")
        celda.tipoTerreno = tipoTerreno

        celda.monstruos = generarListaMonstruoPequeño()

        for (i in generarListaMonstruoGrande()){
            celda.monstruos.add(i)
        }
        celda.pasada = true

        conn.anadirDatosCelda(celda)
    }

    fun generarListaObjetos(id: Int): MutableList<Objeto>{

        var listaObjeto = mutableListOf<Objeto>()

        for (i in 1..2){
            var objeto = Objeto()
            listaObjeto.add(conn.devolverDatosTablaObjeto((id+i), objeto))

            println(listaObjeto[i-1].id)

        }

        return  listaObjeto
    }

    fun generarListaMaterial(id: Int): MutableList<Material>{


        var listaMaterial = mutableListOf<Material>()

        for (i in 1..2){
            var material = Material()
            listaMaterial.add(conn.devolverDatosTablaMaterial((id+i), material))
        }

        return listaMaterial
    }

    fun generarListaMaterial(tipoTerreno: String): MutableList<Material>{
        return conn.devolverArrayDatosTablaMaterial(tipoTerreno)
    }

    fun generarListaMonstruoPequeño(): MutableList<Monstruo> {

        var listaMonstruo = mutableListOf<Monstruo>()

        for (i in 0..1) {
            var monstruo = Monstruo()
            monstruo = conn.devolverDatosTablaMonstruo(Random.nextInt(2, 105), monstruo)

            while (monstruo.tamanio != "0cm") {
                monstruo = conn.devolverDatosTablaMonstruo(Random.nextInt(2, 105), monstruo)
            }
            listaMonstruo.add(monstruo)
        }
        return listaMonstruo
    }

    fun generarListaMonstruoGrande(): MutableList<Monstruo>{


        var listaMonstruo = mutableListOf<Monstruo>()

        for (i in 0..1) {
            var monstruo = Monstruo()
            monstruo = conn.devolverDatosTablaMonstruo(Random.nextInt(2, 105), monstruo)
            while (monstruo.tamanio == "0cm") {
                monstruo = conn.devolverDatosTablaMonstruo(Random.nextInt(2, 105), monstruo)
            }
            listaMonstruo.add(monstruo)
        }
        return listaMonstruo

    }


}

fun main() {
    val controlador = Controlador()

    controlador.crearCeldaEjemplo("Valle")

//    print(controlador.imprimeMonstruo("Malfestio"))
//    controlador.imprimeObjeto("No")
//    print(controlador.imprimeMaterial("Hongo"))
//    print(controlador.imprimeMaterial("Seta depresiva"))
//    print(controlador.imprimeMaterial("Seta excitante"))




    println(controlador.imprimeCelda(3))

//    println("${controlador.conn.devolverUltimoId("Monstruo")} ${controlador.conn.devolverUltimoId("Celda")} ${controlador.conn.devolverUltimoId("Material")}")


}