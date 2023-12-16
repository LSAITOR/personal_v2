package com.example.personal.core

import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

object UtilsDate {

    fun mostrarCalendario(etFecha: EditText, fragmentManager: FragmentManager) {
        // Creamos un objeto (instancia) de MaterialDatePicker
        val picker = MaterialDatePicker.Builder.datePicker().build()

        // Seleccionar la fecha y obtener su valor
        picker.addOnPositiveButtonClickListener {
            etFecha.setText(
                SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(it + 86400000L)
            )
        }

        // Mostrar el calendario
        picker.show(fragmentManager, picker.toString())
    }

}