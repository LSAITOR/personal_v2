package com.example.personal.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.personal.data.model.Cliente
import com.example.personal.databinding.CuadroCantidadBinding
import com.example.personal.databinding.DialogInfoClienteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoClienteDialog : DialogFragment() {

    // Datos a recibir
    private var nombre: String = ""
    private var rfc: String = ""
    private var direccion: String = ""
    private var email: String = ""
    private var estado: String = ""

    companion object {

        // Dentro de companion object es considerado un objeto o estatico.
        fun newInstance(cliente: Cliente): InfoClienteDialog {
            val instancia = InfoClienteDialog()
            instancia.isCancelable = false
            instancia.nombre = cliente.nombre
            instancia.rfc = cliente.rfc
            instancia.direccion = cliente.direccion
            instancia.email = cliente.email
            instancia.estado = if (cliente.estatus == 1) "Vigente" else "No vigente"

            return instancia
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogInfoClienteBinding.inflate(layoutInflater)

        // Pasar los datos a la vista (interfaz grafica)
        binding.tvNombre.text = nombre
        binding.tvRfc.text = rfc
        binding.tvDireccion.text = direccion
        binding.tvEmail.text = email
        binding.tvEstado.text = estado

        // Crear y mostrar el dialogo
        val builder = MaterialAlertDialogBuilder(requireContext())
        // Asignar nuestra interfaz grafica (binding) al dialogo (builder)
        builder.setView(binding.root)

        builder.setTitle("Info Cliente")
        builder.setCancelable(false)

        builder.setPositiveButton("Aceptar"){ dialog, _ ->
            // Cerrar el cuador de dialogo
            dialog.dismiss()
        }

        return builder.create()
    }

}