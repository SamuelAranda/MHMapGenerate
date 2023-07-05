package Modelo

open class Monstruo() {

    var id: Int = 0
    var nombre: String = ""
    var tipo: String = ""
    var tamanio: String = ""
    var descripcion: String = ""
    var rc: Int = 0
    var idTrofeo = 0
    var puntosTrofeo: Int = 0
    var materiales: MutableList<Material>? = mutableListOf()
    var materialesTrofeo: MutableList<Material>? = mutableListOf()


    override fun toString(): String {

        var materialesLista = ""
        var materialesListaTrofeo = ""

        for(i in materiales!!){
            materialesLista += i.nombre + ", "
        }

        for(i in materialesTrofeo!!){
            materialesListaTrofeo += i.nombre + ", "
        }

        return "$id, $nombre, $tipo, Tamaño: $tamanio, RC: $rc, Trofeo: $puntosTrofeo, $descripcion\n" +
                "Lista de materiales: $materialesLista\n" +
                "Materiales del trofeo: $materialesListaTrofeo"
    }

}