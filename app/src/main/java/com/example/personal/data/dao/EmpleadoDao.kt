package com.example.personal.data.dao

import com.example.personal.data.model.Emplea2

object EmpleadoDao {

    // Una funcion  que recibe una dato y retorna una lista
    fun leerEmplea2(dato: String): List<Emplea2> {

       // Crear una lista mutable (puede cambiar sus datos internos) vacia de tipo emplea2
        val lista  = mutableListOf<Emplea2>()

        try {
            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("SELECT ine, nombre, direccion, celular, sueldodia FROM Emplea2 WHERE nombre LIKE '%' + ? + '%';")
            // Asiganr valores a los parametros
            ps.setString(1, dato) // setString --> indica que el parametro sera de tipo string

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeQuery()

            // Recorrer sus filas, mediante un bucke while, se e
            while (rs.next()) {
                lista.add(
                    Emplea2().apply {
                        ine = rs.getString("ine")
                        nombre = rs.getString("nombre")
                        direccion = rs.getString("direccion")
                        celular  = rs.getString("celular")
                        sueldodia = rs.getDouble("sueldodia")
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

    // Funcion que retorne un empleado
    fun obtenerEmplea2(dato: String): Emplea2 {

        // Crear un objeto de nombre empleado y de tipo Emplea2
        val empleado = Emplea2()

        try {
            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("SELECT ine, nombre, direccion, celular, sueldodia FROM Emplea2 WHERE ine = ?;")
            // Asiganr valores a los parametros
            ps.setString(1, dato) // setString --> indica que el parametro sera de tipo string

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeQuery()

            // Recorrer sus filas, mediante un bucke while, se e
            while (rs.next()) {
                empleado.ine = rs.getString("ine")
                empleado.nombre = rs.getString("nombre")
                empleado.direccion = rs.getString("direccion")
                empleado.celular  = rs.getString("celular")
                empleado.sueldodia = rs.getDouble("sueldodia")
            }

            // Cerrar el prepareStatement
            ps.close()

            // Retorna la lista con sus valores
            return empleado
        } catch (e: Exception) {
            // Captura cualquier error dentro del try - catch
            throw Exception(e.message)
        }

    }

    // Eliminar un registro
    fun eliminarEmplea2(entidad: Emplea2): Int {

        // Crear un objeto de nombre empleado y de tipo Emplea2
        val empleado = Emplea2()

        try {
            // Verificar si el empleado existe en otras tablas
            //var verificar = "Select count(idventa) From Ventas WHERE ine=?"
            //var verificar2 = "Select count(idcompra) From compras WHERE ine=?"

            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("DELETE FROM Emplea2 WHERE ine = ?;")
            // Asiganr valores a los parametros
            ps.setString(1, entidad.ine) // setString --> indica que el parametro sera de tipo string

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

    fun grabar(entidad: Emplea2): Int {

        // Crear un objeto de nombre empleado y de tipo Emplea2
        val empleado = Emplea2()

        try {
            // verificar si el ine existe
            val sqlVerificar = "Select count(ine) From Emplea2 WHERE ine=?"
            // Insertar registro
            val sql = "INSERT INTO Emplea2 (ine, nombre, direccion, celular, sueldodia) VALUES (?, ?, ?, ?, ?);"

            // crear un prepareStatement con la consulta sql
            var ps = Conexion.getConexion().prepareStatement(sqlVerificar)
            ps.setString(1, entidad.ine)
            // ejecutar la verificacion
            val rsVerificar = ps.executeQuery()
            // Ubicarnos en la primera posicion --->  rsVerificar.getInt(1) Obtiene el valor de la a posicion 1
            while (rsVerificar.next() && rsVerificar.getInt(1) > 0) {
                rsVerificar.close()
                // Nosotros lanzamos la excepcion
                throw Exception("El numero de INE ya existe en la tabla")
            }

            // Borrar los parametros del ps anterios
            ps.clearParameters()

            // Ejecutar la insercion de datos
            ps = Conexion.getConexion().prepareStatement(sql)
            // Asiganr valores a los parametros
            ps.setString(1, entidad.ine) // setString --> indica que el parametro sera de tipo string
            ps.setString(2, entidad.nombre)
            ps.setString(3, entidad.direccion)
            ps.setString(4, entidad.celular)
            ps.setDouble(5, entidad.sueldodia)

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

    fun actualizar(entidad: Emplea2): Int {

        // Crear un objeto de nombre empleado y de tipo Emplea2
        val empleado = Emplea2()

        try {
            // verificar si el ine existe ---> parametros clave primaria, seguido del campo unico
            //val sqlVerificar = "Select count(ine) From Emplea2 WHERE ine<>? AND nombre=?"

            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion().prepareStatement("UPDATE Emplea2 SET nombre = ?, direccion = ?, celular = ?, sueldodia = ? WHERE ine = ?;")
            // Asiganr valores a los parametros
             // setString --> indica que el parametro sera de tipo string
            ps.setString(1, entidad.nombre)
            ps.setString(2, entidad.direccion)
            ps.setString(3, entidad.celular)
            ps.setDouble(4, entidad.sueldodia)
            ps.setString(5, entidad.ine)

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