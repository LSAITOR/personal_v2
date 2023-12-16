package com.example.personal.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object UtilsCommon {

    fun ocultarTeclado(context: Context, view: View) {
        val imm = context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager

        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun limpiarEditText(view: View) {
        val it = view.touchables.iterator()

        // Recorre todos los elementos encontrados
        while (it.hasNext()) {
            // Avanza al siguiente elemento
            val v = it.next()
            // Si el elmento encontrado es un Edittext
            if (v is EditText) v.setText("") // Asigna un valor vacio
        }
    }

    // Funcion (método) para formatear a dos decimales un valor double, dando como resultado un string
    fun formatearDoubleString(valor: Double): String {
        val formato = DecimalFormat("#0.00") // 0.5 -> 0.50
        val dfs = DecimalFormatSymbols()
        dfs.decimalSeparator = '.'

        formato.decimalFormatSymbols = dfs

        return formato.format(valor).toString()
    }

}