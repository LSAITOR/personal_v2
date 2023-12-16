package com.example.personal.data.dao

import com.example.personal.data.model.Emplea2
import com.example.personal.data.model.Producto

object ProductoDao {

    fun listarProducto(dato: String): List<Producto>? {
        // Crear una lista mutable (puede cambiar sus datos internos) vacia de tipo emplea2
        val lista  = mutableListOf<Producto>()

        try {
            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("SELECT Id, CodigoBarra, Descripcion, Costo, Precio, Stock FROM Producto WHERE Descripcion LIKE '%' + ? + '%';")
            // Asiganr valores a los parametros
            ps.setString(1, dato) // setString --> indica que el parametro sera de tipo string

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeQuery()

            // Recorrer sus filas, mediante un bucke while, se e
            while (rs.next()) {
                lista.add(
                    Producto().apply {
                        id = rs.getInt("Id")
                        codigoBarra = rs.getString("CodigoBarra")
                        descripcion = rs.getString("Descripcion")
                        costo  = rs.getDouble("Costo")
                        precio = rs.getDouble("Precio")
                        stock = rs.getInt("Stock")
                    }
                )
            }

            // Cerrar el prepareStatement
            ps.close()

            // Retorna la lista con sus valores
            return lista
        } catch (e: Exception) {
            // Captura cualquier error dentro del try - catch
            throw Exception(e.message)
        }
    }

    fun eliminar(entidad: Producto): Int {



        try {
            // Verificar si el empleado existe en otras tablas
            //var verificar = "Select count(idventa) From Ventas WHERE ine=?"
            //var verificar2 = "Select count(idcompra) From compras WHERE ine=?"

            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("DELETE FROM Producto WHERE id = ?;")
            // Asiganr valores a los parametros
            ps.setInt(1, entidad.id) // int --> indica que el parametro sera de tipo entero

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeUpdate()

            // Cerrar el prepareStatement
            ps.close()

            // Retorna la lista con sus valores
            return if(rs > 0) 1 else 0
        } catch (e: Exception) {
            // Captura cualquier error dentro del try - catch
            throw Exception(e.message)
        }

    }


    fun grabar(entidad: Producto): Int {

        // Crear un objeto de nombre empleado y de tipo Emplea2
        //val empleado = Emplea2()

        try {
            // verificar si el ine existe
            val sqlVerificar = "Select count(id) From Producto WHERE codigoBarra=?"
            // Insertar registro
            val sql = "INSERT INTO Producto ( codigoBarra, descripcion, costo, precio, stock) VALUES (?, ?, ?, ?, ?);"

            // crear un prepareStatement con la consulta sql
            var ps = Conexion.getConexion().prepareStatement(sqlVerificar)
            ps.setString(1, entidad.codigoBarra)
            // ejecutar la verificacion
            val rsVerificar = ps.executeQuery()
            // Ubicarnos en la primera posicion --->  rsVerificar.getInt(1) Obtiene el valor de la a posicion 1
            while (rsVerificar.next() && rsVerificar.getInt(1) > 0) {
                rsVerificar.close()
                // Nosotros lanzamos la excepcion
                throw Exception("El codigo de barra ya existe en la tabla")
            }

            // Borrar los parametros del ps anterios
            ps.clearParameters()

            // Ejecutar la insercion de datos
            ps = Conexion.getConexion().prepareStatement(sql)
            // Asiganr valores a los parametros
            ps.setString(1, entidad.codigoBarra) // setString --> indica que el parametro sera de tipo string
            ps.setString(2, entidad.descripcion)
            ps.setDouble(3, entidad.costo)
            ps.setDouble(4, entidad.precio)
            ps.setInt(5, entidad.stock)

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeUpdate()

            // Cerrar el prepareStatement
            ps.close()

            // Retorna la lista con sus valores
            return if(rs > 0) 1 else 0
        } catch (e: Exception) {
            // Captura cualquier error dentro del try - catch
            throw Exception(e.message)
        }

    }

    fun actualizar(entidad: Producto): Int {
        try {
            // verificar si el ine existe ---> parametros clave primaria, seguido del campo unico
            //val sqlVerificar = "Select count(ine) From Emplea2 WHERE ine<>? AND nombre=?"

            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("UPDATE Producto SET codigoBarra = ?, descripcion = ?, costo = ?, precio = ?, stock = ? WHERE id = ?;")
            // Asiganr valores a los parametros
            // setString --> indica que el parametro sera de tipo string
            ps.setString(1, entidad.codigoBarra)
            ps.setString(2, entidad.descripcion)
            ps.setDouble(3, entidad.costo)
            ps.setDouble(4, entidad.precio)
            ps.setInt(5, entidad.stock)
            ps.setInt(6, entidad.id)

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeUpdate()

            // Cerrar el prepareStatement
            ps.close()

            // Retorna la lista con sus valores
            return if(rs > 0) 1 else 0
        } catch (e: Exception) {
            // Captura cualquier error dentro del try - catch
            throw Exception(e.message)
        }

    }

}