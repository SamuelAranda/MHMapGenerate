package Modelo

open class Objeto() {
    var id: Int? = 0
    var nombre: String? = ""
    var precio: Int? = 0
    var artesania: String? = ""
    var exito: Int? = 0
    var lvl: Int? = 0
    var materiales: MutableList<Material?>? = mutableListOf()
    var descripcion: String? = ""



    override fun toString(): String {
        return "$id, $nombre, $precio monedas de plata, $artesania: $exito, lvl: $lvl, $descripcion\n" +
                "Ingerdientes: ${materiales?.get(0)!!.nombre} " +
                "${materiales?.get(1)!!.nombre}\n"
    }
}