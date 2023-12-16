package com.example.personal.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.DialogFragment
import com.example.personal.databinding.CuadroCantidadBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Es una clase que hereda de Dialogfragment
class CantidadDialog : DialogFragment() {

    // Declarar unas variables
    private var titulo: String = ""
    private var mensaje: String = ""
    private var precio: Double = 0.0
    // lateinit -> es una promesa que nosostros vamos a proporcionar ese valor mas adelante, pero antes de usarlo
    private lateinit var iEnviarListener: IEnviarListener


    // Creamos una interfaz para enviar al carrito de compra, solo los datos
    // que pueden cambiar (cantidad, precio)
    interface IEnviarListener {
        // Envia dos datos (cantidad y el precio)
        fun enviarItem(cantidad: Int, precio: Double)
    }

    // Crear una instancia de CantidadDialog, para poder visualizar (Como crear objetos estaticos)
    companion object {
        private var cantidad: Int = 1

        fun newInstance(producto: String, _precio: Double, _iOnEnviarListener: IEnviarListener): CantidadDialog {
            val instancia = CantidadDialog()
            instancia.titulo = producto
            instancia.mensaje = "Selecc. una cantidad"
            instancia.precio=_precio
            instancia.isCancelable = false
            cantidad = 1
            // asignamos un valor el listener (cumpliendo con la promesa)
            instancia.iEnviarListener = _iOnEnviarListener

            return instancia
        }
    }

    // Agregamos el onCreateDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Enlazar con la interfaz grafica que sera inflado con CuadroCantidadBinding y almacena la
        // raiz en binding
        val binding = CuadroCantidadBinding.inflate(layoutInflater)

        binding.etPrecio.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        binding.tvCantidad.setText(cantidad.toString())
        binding.etPrecio.setText(precio.toString())

        val builder = MaterialAlertDialogBuilder(requireContext())
        // Asigna una vista personalizada, accediendo a todos los controles
        builder.setView(binding.root)

        builder.setTitle(titulo)
        builder.setMessage(mensaje)

        binding.ibMenos.setOnClickListener {
            if(cantidad > 1) {
                cantidad-- // Disminuye la cantidad en una unidad (cantidad = cantidad - 1)
                binding.tvCantidad.setText(cantidad.toString())
            }
        }

        binding.ibMas.setOnClickListener {
            cantidad++ // Aumenta la cantidad en una unidad (cantidad = cantidad + 1)
            binding.tvCantidad.setText(cantidad.toString())
        }

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            if(binding.etPrecio.text.isNullOrEmpty()){
                binding.etPrecio.setText(precio.toString())
            }

            if(binding.etPrecio.text.toString().toDouble() == 0.0) {
                binding.etPrecio.setText(precio.toString())
            }

            iEnviarListener.enviarItem(
                binding.tvCantidad.text.toString().toInt(),
                binding.etPrecio.text.toString().toDouble()
            )

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            iEnviarListener.enviarItem(0, 0.0)
            dialog.dismiss()
        }

        return builder.create()
    }
}