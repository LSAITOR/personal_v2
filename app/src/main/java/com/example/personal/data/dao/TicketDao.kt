package com.example.personal.data.dao

import com.example.personal.data.model.DetalleTicket
import com.example.personal.data.model.Producto
import com.example.personal.data.model.Ticket
import java.sql.Date
import java.sql.PreparedStatement

object TicketDao {

    // Esta funcion (grabar) retornara (: Int) un valor entero
    // Esta funcion manejara transacciones
    fun grabar(entidad: Ticket): Int {
        // Objeto conexion
        val cn = Conexion.getConexion()

        try {
            // Verificar si el detalle esta vacio
            if (entidad.detalle.isEmpty())
                throw Exception("El ticket no cuenta con items en su detalle.")

            // Sentencias que impactaran en las tres entidades (tablas)
            val insertarTicket =
                "INSERT INTO Ticket(fecha, cliente, total, estado) VALUES (?, ?, ?, ?);"

            val insertarDetalle =
                "INSERT INTO DetalleTicket(idticket, idproducto, cantidad, costo, precio, importe) VALUES (?, ?, ?, ?, ?, ?);"

            val actulizarStockProducto = "UPDATE Producto SET stock = stock - ? WHERE id = ?;"

            //Inicia la transaccion
            cn.autoCommit = false

            var ps = cn.prepareStatement(insertarTicket, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setDate(1, entidad.fecha)
            ps.setString(2, entidad.cliente)
            ps.setDouble(3, entidad.total)
            ps.setString(4, entidad.estado)

            // Ejecutar el ps
            ps.executeUpdate()

            // Recuperar el id (key) generado
            val rs = ps.generatedKeys

            // Guardar el id obtenido
            var keyTicket = 0
            if (rs.next()) keyTicket = rs.getInt(1)
            rs.close()

            // Creamos un nuevo prepareStatement
            ps = cn.prepareStatement(insertarDetalle)
            // Limpiamos los parametros anteriores que ya no usaremos
            ps.clearParameters()

            // Crear otro prepareStatement para actualizar el stock
            val psStock = cn.prepareStatement(actulizarStockProducto)

            // Usaremos un bucle for para recuperar todos los elementos del detalle del ticket
            for (det in entidad.detalle) {
                ps.setInt(1, keyTicket)
                ps.setInt(2, det.mproducto?.id ?: 0)  // Operador elvis ?: Si el valor esta nulo , entonces asignamos un valor cero (0)
                ps.setInt(3, det.cantidad)
                ps.setDouble(4, det.costo)
                ps.setDouble(5, det.precio)
                ps.setDouble(6, det.importe)

                // Ejecutar la sentencia
                ps.executeUpdate()
                // Limpiar los parametros
                ps.clearParameters()

                // Agregar los nuevos parametros para el prepareStatement stock
                psStock.setInt(1, det.cantidad)
                psStock.setInt(2, det.mproducto?.id ?: 0) // Operador elvis ?: Si el valor esta nulo , entonces asignamos un valor cero (0)
                // Ejecutar la sentencia
                psStock.executeUpdate()
                // Limpiar los parametros
                psStock.clearParameters()
            }

            // Si no existe error hasta este punto, entonces confirmar los cambios en la DB
            cn.commit()
            cn.autoCommit = true

            return if (keyTicket > 0) keyTicket
            else
                throw Exception("Error desconocido, no se pudo realizar la operación")
        } catch (e: Exception) {
            try {
                // Deja la base de datos tal y como lo encontro antes de las inserciones de datos.
                cn.rollback()
            } catch (ex: Exception) {
                throw Exception(ex.message)
            }

            throw Exception(e.message)
        }
    }

    fun anularTicket(idTicket: Int): Int {
        try {
            val anularTicket =
                "UPDATE Ticket SET estado = 'ANULADO' WHERE id = ?;"

            val ps = Conexion.getConexion().prepareStatement(anularTicket)
            ps.setInt(1, idTicket)

            return ps.executeUpdate()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    fun listarTicket(desde: String, hasta: String): List<Ticket> {
        val lista = mutableListOf<Ticket>()

        try {
            // crear un prepareStatement con la consulta sql
            val ps = Conexion.getConexion()
                .prepareStatement("SELECT Id, fecha, cliente, estado, total FROM Ticket WHERE fecha >= ? AND fecha <= ?;")
            // Asiganr valores a los parametros
            ps.setString(1, desde)
            ps.setString(2, hasta)

            // Ejecutar la consulta y alcemar el resultado en el resultset
            // val indica que es una constante
            // var indica que su valor puede cambiar
            val rs = ps.executeQuery()

            // Recorrer sus filas, mediante un bucke while, se e
            while (rs.next()) {
                lista.add(
                    Ticket().apply {
                        id = rs.getInt("Id")
                        fecha = rs.getDate("fecha")
                        cliente = rs.getString("cliente")
                        estado = rs.getString("estado")
                        total = rs.getDouble("total")
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

    // esta funcion recibe como parametro un idticket y nos va a retornar una lista de detalle del ticket
    fun listarDetalleTicket(idTicket: Int): List<DetalleTicket> {
        // Preparar la lista, estara vacia y ademas es mutable (mutableListOf) puede cambiar su contenido
        val lista = mutableListOf<DetalleTicket>()

        // proteger las siguientes lineas de codigo, ya que este podria generar error (try- catch)
        try {
            // preparar la consulta usando la conexion a la base de datos.
            // Estos datos seran obtenidos de las tablas producto y detalleticket, bajo la condicion del idticket
            val ps = Conexion.getConexion().prepareStatement(
                "SELECT CodigoBarra, descripcion, cantidad, DetalleTicket.costo, DetalleTicket.precio, importe FROM Producto INNER JOIN DetalleTicket ON Producto.id = DetalleTicket.idproducto WHERE idTicket=?;"
            )

            // Agregando parametros
            ps.setInt(1, idTicket)

            // Ejecutar la consulta y la obtiene en un resultset
            val rs = ps.executeQuery()

            // Recorrer los datos del rs, hasta el ultimo items (elemento), con next() pasara al siguiente registro
            while (rs.next()) {
                // Crear los objetos de las entidades (detalleticket y producto)

                // Crear el objeto detalle (detalleticket)
                val oDetalle = DetalleTicket(
                    cantidad = rs.getInt("cantidad"),
                    precio = rs.getDouble("precio"),
                    costo = rs.getDouble("costo"),
                    importe = rs.getDouble("importe")
                )

                // Crea el objeto producto (Producto)
                val oProducto = Producto(
                    descripcion = rs.getString("descripcion"),
                    codigoBarra = rs.getString("CodigoBarra")
                )

                // Al objeto oDetalle, agregamos el objeto producto a través de su campo mproducto
                oDetalle.mproducto = oProducto

                // Finalmente agrega el objeto oDetalle a la lista
                lista.add(oDetalle)

                // Agregar a la lista un objeto de tipo detalletocket
                /*lista.add(
                    // Crea el objeto detalleticket, con apply damos valores a sus campos
                    DetalleTicket().apply {
                        cantidad = rs.getInt("cantidad")
                        precio = rs.getDouble("precio")
                        costo = rs.getDouble("costo")
                        importe = rs.getDouble("importe")
                        mproducto = Producto().apply {
                            descripcion = rs.getString("descripcion")
                        } // Crea el objeto producto
                    }
                )*/
            }

            // Cerrar el rs y el ps
            rs.close()
            ps.close()

            // Retornar la lista de elementos leidos u obtenidos en la consulta select
            return lista
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}

/*val objDetalle = DetalleTicket().apply {
                  cantidad = rs.getInt("cantidad")
                  precio = rs.getDouble("precio")
                  costo = rs.getDouble("costo")
                  importe = rs.getDouble("importe")
                  mproducto = Producto().apply { descripcion = rs.getString("descripcion") }
              }*/

/*val xc = DetalleTicket(
    cantidad = rs.getInt("cantidad"),
    precio = rs.getDouble("precio"),
    costo = rs.getDouble("costo"),
    importe = rs.getDouble("importe"),
    mproducto = Producto(descripcion = rs.getString("descripcion"))
)*/