package com.example.personal.data.dao

import com.example.personal.data.model.Cliente

// 2. Crear el ClienteDao, que sera el encargado de ejecutar las consultas (select) y las sentencias (insert, delete, update) hacia la base de datos
object ClienteDao {

    // CRear las funcionalidades que tendra el dao

    fun registrar(entidad: Cliente): Int {
        try {
            // crear un prepareStatement con la senetencia
            val ps = Conexion.getConexion().prepareStatement("Insert into cliente(nombre, rfc, direccion, email, estatus) VALUES(?, ?, ?, ?, ?)")

            // Asignar valores a los parametros
            ps.setString(1, entidad.nombre)
            ps.setString(2, entidad.rfc)
            ps.setString(3, entidad.direccion)
            ps.setString(4, entidad.email)
            ps.setInt(5, entidad.estatus)

            // Ejecutar el ps
            return ps.executeUpdate()
        } catch (e: Exception){
            // Lanzar una excepcion hacia la funcion que lo llamo.
            throw Exception(e.message)
        }
    }

    fun listar(dato: String): List<Cliente>{
        // Crear una lista vacia de tipo cliente
        val lista= mutableListOf<Cliente>()

        try {
            val ps=Conexion.getConexion().prepareStatement("SELECT id, nombre, rfc, direccion, email, estatus FROM cliente WHERE nombre Like '%' + ? + '%'")

            // asignar valor al parametro
            ps.setString(1, dato)

            // Ejecutat el ps y el resultado sera almacenado en un resultset (rs)
            val rs=ps.executeQuery()

            //Recorrer el rs, y asignar cada registro a la lista que hemos creado
            while (rs.next()){
                val cliente = Cliente()
                cliente.id = rs.getInt("id")
                cliente.nombre = rs.getString("nombre")
                cliente.rfc = rs.getString("rfc")
                cliente.email = rs.getString("email")
                cliente.direccion = rs.getString("direccion")
                cliente.estatus = rs.getInt("estatus")

                lista.add(cliente)
            }
            
            rs.close()
            ps.close()

            // Retornar la lista
            return lista
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

}