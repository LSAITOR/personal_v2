package com.example.personal.data.dao

import com.example.personal.data.model.Usuario
import java.sql.ResultSet

// Segundo paso. crear el objeto dao para ejecutar consultas y/o sentencias SQL
// Impactara directamente en la bd.
object UsuarioDao {

    // Usuario? quiere decir que puede retornar un usuario nulo (No existe el usuario en la DB)
    fun login(usuario: String, clave: String): Usuario {
        // Conexion.getConexion().prepareStatement(anularTicket)
        var mUsuario: Usuario? = null

        try {
            // Preparamos la consulta segun la vigencia, nick y clave del usario para validar
            // Si existe en la db Usuario
            val ps = Conexion.getConexion().prepareStatement(
                "SELECT id, nombre, nick FROM Usuario WHERE vigente=1 AND nick=? AND clave=?;"
            )
            // Pasar los parametros a la consulta
            ps.setString(1,usuario)
            ps.setString(2, clave)

            //Ejecutar la consulta y almacena el resultado en el resultset (rs)
            val rs = ps.executeQuery()

            // Leer los datos del resultset (rs)
            if(rs.next()){
                // Crear el objeto usuario
                mUsuario = Usuario()
                // asignando valores al objeto mUsuario
                mUsuario.id= rs.getInt("id")
                mUsuario.nombre=rs.getString("nombre")
                mUsuario.nick= rs.getString("nick")
            }

            // Cerramos los objetos usados
            rs.close()
            ps.close()

            //Si vigente, nick y la clave no son validos mostrar uan excepcion
            if(mUsuario==null)
                throw Exception("Las credenciales de acceso no son validos.")

            // Si llega a este punto, quiere decir que el usuario existe, es decir el
            // nick y la clave son correctas, obviamente esta vigente (vigente=1)
            return mUsuario
        } catch (e: Exception){
            // Lanza una excepcion, con el error capturado (e.message)
            throw Exception(e.message)
        }
    }

    // Consultar usuarios segun su nombre y este retornara una lista de usuario
    fun listarUsuario(dato: String): List<Usuario>{


        return emptyList()
    }

}