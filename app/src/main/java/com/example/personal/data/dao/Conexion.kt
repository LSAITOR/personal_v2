package com.example.personal.data.dao

import android.os.StrictMode
import com.example.personal.core.PreferenciasKey
import com.example.personal.core.ProveedorPreferencia
import com.example.personal.core.UtilsSecurity
import java.sql.Connection
import java.sql.DriverManager

object Conexion {
/*
    // Creando objeto nulo
    private var cnn: Connection? = null

    // Este es un constructor (init)
    init {
        try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            // Crea una instancia del drive
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()

            // Asigna el dato de la conexion al servidor
            cnn = DriverManager.getConnection(
                "jdbc:jtds:sqlserver://192.168.0.6:1433;databaseName=CONSURTARBD;user=Saitorod;password=Daniel08;"
            )
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
    */

    // Una funcion que obtiene la cadena la coenxion de tipo Conexion
    fun getConexion(): Connection {
        //return cnn!!
        try {
            // Crea una instancia del drive
            Class.forName("net.sourceforge.jtds.jdbc.Driver")

            // Asigna el dato de la conexion al servidor
            /*return DriverManager.getConnection(
                "jdbc:jtds:sqlserver://192.168.0.6:1433;databaseName=CONSURTARBD;user=ssaito;password=Daniel08;"
            )*/

            return DriverManager.getConnection(
                UtilsSecurity.descifrarDato(
                    ProveedorPreferencia.getPreferencia(PreferenciasKey.CONFIGURAR_SERVER) ?: ""
                )
            )
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}