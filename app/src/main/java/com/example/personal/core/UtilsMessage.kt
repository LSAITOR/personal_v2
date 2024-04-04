package com.example.personal.core

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object UtilsMessage {

    // Nuestra funcion recibira un titulo (que puede ser nulo), seguido de un mensaje y un contexto(activity, fragment, dialogfragment)
    fun showAlert(titulo: String?, mensaje: String, contexto: Context){
        // Creacion del objeto (builder) a partir de MaterialAlertDialogBuilder
        val builder = MaterialAlertDialogBuilder(contexto)

        // Pasando parametros a nuestro builder
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        // Debe ser cerrado manualmente por el usuario
        builder.setCancelable(false)

        // La accion que realiza al hacer clic en el boton "OK"
        builder.setPositiveButton("OK"){dialog, _ ->
            // Cerrar el mensaje
            dialog.cancel()
        }

        // mostrar el mensaje
        builder.create().show()
    }

}