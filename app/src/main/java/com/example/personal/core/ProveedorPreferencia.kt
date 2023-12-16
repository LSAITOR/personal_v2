package com.example.personal.core

import android.content.Context
import com.example.personal.PersonalApp

object ProveedorPreferencia {
    // Una funcion para escribir un archivo privado en la carpeta de instalacion de la app
    fun setConfigurarServer(cadenaConexion: String){
        // Crear un editor o escritor
        val editor = PersonalApp.getAppContext().getSharedPreferences(
            Constantes.PREFERENCIA_APP, Context.MODE_PRIVATE
        ).edit()

        // Al editor le pasaremos la cadena de conexion
        editor.putString(
            PreferenciasKey.CONFIGURAR_SERVER.name,
            cadenaConexion.trim()
        )

        // Confirmar la escritura
        editor.apply()
    }

    // Una funcion para leer los "campos" del archivo de preferencias
    fun getPreferencia(key: PreferenciasKey): String?{
        // Leer el dato almacenado en la llave (key)que recibimos por parametro
        // Si esta no existe, entonces devolver un valor vacio
        return PersonalApp.getAppContext().getSharedPreferences(
            Constantes.PREFERENCIA_APP, Context.MODE_PRIVATE
        ).getString(key.name, "")
    }
}