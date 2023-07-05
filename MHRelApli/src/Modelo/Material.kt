package Modelo

open class Material() {

    var id: Int = 0
    var nombre: String = ""
    var precio: Int = 0
    var recogida: Int = 0
    var tipo: String = ""
    var descripcion: String = ""
    var lugares: MutableList<String>? = mutableListOf()
    var objetos: MutableList<Objeto>? = mutableListOf()

    override fun toString(): String {

        var listaObjetos: String = ""

        for (i in objetos!!){
            listaObjetos += i.nombre + ", "
        }

        return "$id, $nombre, $descripcion, $precio monedas de plata, Superviviencia: $recogida, $tipo, \n" +
                "$lugares\n Se usa en: " + listaObjetos
    }

}