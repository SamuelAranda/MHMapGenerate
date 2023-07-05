package BBDD

import Modelo.Celda
import Modelo.Material
import Modelo.Monstruo
import Modelo.Objeto
import java.sql.*
import kotlin.random.Random

class DBManagerNuevoPedido {
    var conn: Connection? = null
    var stmnt: Statement? = null
    val listaLugares: ArrayList<String> = arrayListOf()


    fun connectToDataBase() {

        listaLugares.add("Bosque Rocoso")
        listaLugares.add("Bosque Rocoso")
        listaLugares.add("Selva Jurásica")
        listaLugares.add("Ciénaga")
        listaLugares.add("Estepa Otoñal")
        listaLugares.add("Estepa Otoñal")
        listaLugares.add("Bosque Rocoso")
        listaLugares.add("Picos Nublados")
        listaLugares.add("Selva Jurásica")
        listaLugares.add("Ciénaga")
        listaLugares.add("Volcán")
        listaLugares.add("Dunas")
        listaLugares.add("Canal Helado")
        listaLugares.add("Isla Desierta")
        listaLugares.add("Isla Desierta")
        listaLugares.add("Fronda Arcaica")
        listaLugares.add("Bosque Rocoso")

        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\s.aranda.cabezas\\Desktop\\Instaladoresarchivosetc\\SQLiteDatabaseBrowserPortable\\Data\\MHRel.db")
            } catch (cnfex: ClassNotFoundException) {
                cnfex.printStackTrace()
            } catch (sqlex: SQLException) {
                sqlex.printStackTrace()
            }
        }

        println(conn!!.getClientInfo())

    }

    fun disconnectFromDataBase() {
        stmnt?.close()
        conn?.close()
        conn = null
    }

    fun devolverIdCualquiera(nombre: String, tabla: String): Int {

        var preparedStatement = conn!!.prepareStatement("SELECT ID FROM $tabla " +
                "WHERE Nombre = '$nombre';")

        var rs = preparedStatement.executeQuery()

        rs.next()
        return rs.getInt("ID")

    }

    fun devolverDatosTablaMonstruo(id: Int, peticion: Monstruo): Monstruo{
        var monstruo = peticion

        var preparedStatement = conn!!.prepareStatement("SELECT * FROM Monstruo " +
                "WHERE ID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            monstruo.id = rs.getInt("ID")
            monstruo.nombre = rs.getString("Nombre")
            monstruo.tipo = rs.getString("Tipo")
            monstruo.rc = rs.getInt("RC")
            monstruo.tamanio = rs.getString("Tamaño")
            monstruo.idTrofeo = rs.getInt("TrofeoID")
            monstruo.puntosTrofeo = devolverDatosPuntoTrofeo(monstruo.idTrofeo)
            monstruo.materialesTrofeo = devolverTablaMaterialTrofeo(monstruo.idTrofeo)
            monstruo = devolverDatosMonstruoMaterial(monstruo, monstruo.id)

        }
        return monstruo

    }

    fun devolverDatosTablaCelda(id: Int, peticion: Celda): Celda{

        var celda = peticion

        var preparedStatement = conn!!.prepareStatement("SELECT * FROM Celda " +
                "WHERE ID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            celda.id = rs.getInt("ID")
            celda.evento = devolverDatosEvento(rs.getInt("EventoID"))
            celda.tipoTerreno = devolverDatosTerreno(rs.getInt("TipoTerrenoID"))
            celda.pasada = rs.getBoolean("Pasada")
        }

        preparedStatement = conn!!.prepareStatement("SELECT MaterialID FROM MaterialCelda " +
                "WHERE CeldaID = $id;")

        rs = preparedStatement.executeQuery()

        while (rs.next()) {
            val material = Material()
            celda.material.add(devolverDatosTablaMaterial(rs.getInt("MaterialID"), material))
        }

        preparedStatement = conn!!.prepareStatement("SELECT MonstruoID FROM MonstruoCelda " +
                "WHERE CeldaID = $id;")

        rs = preparedStatement.executeQuery()

        while (rs.next()) {
            val monstruo = Monstruo()
            celda.monstruos.add(devolverDatosTablaMonstruo(rs.getInt("MonstruoID"), monstruo))
        }

        preparedStatement = conn!!.prepareStatement("SELECT ObjetoID FROM ObjetoCelda " +
                "WHERE CeldaID = $id;")

        rs = preparedStatement.executeQuery()

        while (rs.next()) {
            val objeto = Objeto()
            celda.objetos.add(devolverDatosTablaObjeto(rs.getInt("ObjetoID"), objeto))
        }




        return celda


    }

    fun devolverDatosTablaObjeto(id: Int, peticion: Objeto): Objeto{
        val objeto = peticion
        var material1 = Material()
        var material2 = Material()

        var preparedStatement = conn!!.prepareStatement("SELECT * FROM Objeto " +
                "WHERE ID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            objeto.id = rs.getInt("ID")
            objeto.nombre = rs.getString("Nombre")
            objeto.precio = rs.getInt("Precio")
            objeto.artesania = rs.getString("Artesania")
            objeto.exito = rs.getInt("Exito")
            objeto.lvl = rs.getInt("Lvl")
            objeto.descripcion = rs.getString("Descripcion")

            objeto.materiales!!.add(devolverDatosTablaMaterial(rs.getInt("Material1ID"), material1))
            objeto.materiales!!.add(devolverDatosTablaMaterial(rs.getInt("Material2ID"), material2))

        }

        return objeto

    }

    fun devolverDatosTablaMaterial(id: Int, peticion: Material): Material{
        var material = peticion

        var preparedStatement = conn!!.prepareStatement("SELECT * FROM Material " +
                "WHERE ID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            material.id = rs.getInt("ID")
            material.nombre = rs.getString("Nombre")
            material.precio = rs.getInt("Precio")
            material.recogida = rs.getInt("Recogida")
            material.tipo = rs.getString("Tipo")
            material.descripcion = rs.getString("Descripcion")

            material = devolverDatosLugarMaterial(material, material.id)

        }

        return material
    }

    fun devolverArrayDatosTablaMaterial(tipoTerreno: String): MutableList<Material>{
        var listaMaterial = mutableListOf<Material>()

        var preparedStatement = conn!!.prepareStatement("SELECT * FROM Material " +
                "INNER JOIN MaterialTerreno " +
                "ON Material.ID = MaterialTerreno.MaterialID " +
                "WHERE MaterialTerreno.TerrenoID = ${devolverIdCualquiera(tipoTerreno, "TipoTerreno")};")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            var material = Material()

            material.id = rs.getInt("ID")
            material.nombre = rs.getString("Nombre")
            material.precio = rs.getInt("Precio")
            material.recogida = rs.getInt("Recogida")
            material.tipo = rs.getString("Tipo")
            material.descripcion = rs.getString("Descripcion")

            material = devolverDatosLugarMaterial(material, material.id)

            listaMaterial.add(material)

        }

        return listaMaterial
    }

    fun devolverDatosLugarMaterial(peticion: Material, id: Int): Material{
        var material = peticion

        var preparedStatement = conn!!.prepareStatement("SELECT ID, Nombre FROM TipoTerreno " +
                "INNER JOIN MaterialTerreno " +
                "ON TipoTerreno.ID = MaterialTerreno.TerrenoID " +
                "WHERE MaterialTerreno.MaterialID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            material.lugares!!.add(rs.getString("Nombre"))
        }
        return material
    }

    fun devolverDatosMonstruoMaterial(peticion: Monstruo, id: Int): Monstruo{
        var monstruo = peticion

        var preparedStatement = conn!!.prepareStatement("SELECT ID FROM Material " +
                "INNER JOIN MaterialMonstruo " +
                "ON Material.ID = MaterialMonstruo.MaterialID " +
                "WHERE MaterialMonstruo.MonstruoID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            var material = Material()
            monstruo.materiales!!.add(devolverDatosTablaMaterial(rs.getInt("ID"), material))
        }
        return monstruo
    }

    fun devolverDatosObjetoMaterial(id: Int): MutableList<Objeto>{
        var listaObjeto: MutableList<Objeto> = mutableListOf()

        var preparedStatement = conn!!.prepareStatement("SELECT ID FROM Objeto " +
                "WHERE Material1ID = $id OR Material2ID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            var objeto = Objeto()
            listaObjeto.add(devolverDatosTablaObjeto(rs.getInt("ID"), objeto))
        }
        return listaObjeto
    }

    fun devolverDatosPuntoTrofeo(id: Int): Int{

        var preparedStatement = conn!!.prepareStatement("SELECT Puntos FROM Trofeo " +
                "WHERE ID = $id;")
        var rs = preparedStatement.executeQuery()

        return rs.getInt("Puntos")
    }

    fun devolverTablaMaterialTrofeo(id: Int): MutableList<Material> {

        var listaMaterial = mutableListOf<Material>()

        var preparedStatement = conn!!.prepareStatement("SELECT MaterialID FROM MaterialTrofeo " +
                "WHERE TrofeoID = $id;")

        var rs = preparedStatement.executeQuery()

        while (rs.next()) {
            var material = Material()
            listaMaterial.add(devolverDatosTablaMaterial(rs.getInt("MaterialID"), material))
        }
        return listaMaterial
    }

    fun devolverDatosEvento(id: Int): String{

        var rs =  conn!!.prepareStatement("SELECT * FROM Evento " +
                "WHERE ID = $id;").executeQuery()

        rs.next()

        var evento = rs.getString("Nombre")

        rs =  conn!!.prepareStatement("SELECT * FROM TipoEvento " +
                "WHERE ID = ${rs.getInt("TipoEventoID")};").executeQuery()

        rs.next()

        var tipoEvento = rs.getString("Tipo")

        return "$evento, $tipoEvento"


    }

    fun devolverDatosEvento(tipoEvento: String): String{

        var listaEventos = mutableListOf<String>()
        var numAleatorio = 0

       var rs =  conn!!.prepareStatement("SELECT * FROM TipoEvento " +
                "WHERE Tipo = '$tipoEvento';").executeQuery()

        rs.next()

        var id = rs.getInt("ID")

        rs =  conn!!.prepareStatement("SELECT * FROM Evento " +
                "WHERE TipoEventoID = $id;").executeQuery()

        while (rs.next()){
            listaEventos.add(rs.getString("Nombre"))
        }

        numAleatorio = Random.nextInt(0, listaEventos.size-1)

        return "${listaEventos[numAleatorio]}, $tipoEvento"

    }

    fun devolverDatosTerreno(id: Int): String{

        var rs =  conn!!.prepareStatement("SELECT Nombre FROM TipoTerreno " +
                "WHERE ID = $id;").executeQuery()

        rs.next()

        var tipoTerreno = rs.getString("Nombre")

        return tipoTerreno
    }

    fun devolverUltimoId(tabla: String): Int{

        var rs =  conn!!.prepareStatement("SELECT MAX(ID) AS ID FROM $tabla;").executeQuery()
        rs.next()

        return rs.getInt("ID")

    }

    fun anadirDatosCelda(celda: Celda){

        var agregarCosas = conn!!.createStatement()

        var evento = celda.evento!!.split(", ")[0]

        var idEvento = devolverIdCualquiera(evento, "Evento")
        var idTerreno = devolverIdCualquiera(celda.tipoTerreno!!, "TipoTerreno")

        agregarCosas.executeUpdate("INSERT INTO Celda " +
                "(ID, EventoID, TipoTerrenoID, Pasada)\n" +
                "VALUES (${celda.id}, $idEvento, $idTerreno, ${celda.pasada});")


        for (i in celda.material){

            agregarCosas.executeUpdate("INSERT INTO MaterialCelda " +
                    "(MaterialID, CeldaID)\n" +
                    "VALUES (${i.id}, ${celda.id});")
        }

        for (i in celda.monstruos){
            agregarCosas.executeUpdate("INSERT INTO MonstruoCelda " +
                    "(MonstruoID, CeldaID)\n" +
                    "VALUES (${i.id}, ${celda.id});")
        }

        for (i in celda.objetos){
            agregarCosas.executeUpdate("INSERT INTO ObjetoCelda " +
                    "(ObjetoID, CeldaID)\n" +
                    "VALUES (${i.id}, ${celda.id});")
        }




    }



//    fun devolverDatosTabla()
//    fun devolverDatosTabla()
//    fun devolverDatosTabla()
//    fun devolverDatosTabla()
//    fun devolverDatosTabla()
//    fun devolverDatosTabla()
//    fun devolverDatosTabla()
//    fun devolverDatosTabla()




}
