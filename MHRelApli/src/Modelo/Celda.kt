package Modelo

class Celda() {
    var id: Int? = null
    var evento: String? = null
    var tipoTerreno: String? = null
    var monstruos: MutableList<Monstruo> = mutableListOf()
    var objetos: MutableList<Objeto> = mutableListOf()
    var material: MutableList<Material> = mutableListOf()
    var pasada: Boolean = false

    constructor(tipoTerreno: String) : this() {
        this.tipoTerreno = tipoTerreno
    }

    override fun toString(): String {

        var listaMonstruo = ""
        var listaObjeto = ""
        var listaMaterial = ""

        for (i in monstruos){
            listaMonstruo += "${i.nombre} :: RC: ${i.rc}\n"
        }

        for (i in objetos){
            listaObjeto += i.nombre + ", "
        }

        for (i in material){
            listaMaterial += "${i.nombre} :: Superviviencia: ${i.recogida}\n"
        }

        return "$id ::: Evento: $evento, $tipoTerreno\n" +
                "Monstruos: $listaMonstruo\n" +
                "Objetos: $listaObjeto\n" +
                "Materiales: $listaMaterial\n" +
                "¿Pasada? $pasada"
    }

}