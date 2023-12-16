package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.personal.core.UtilsCommon
import com.example.personal.data.dao.ProductoDao
import com.example.personal.data.model.Producto
import com.example.personal.databinding.FragmentOperacionProductoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OperacionProductoFragment : Fragment() {

    private lateinit var binding: FragmentOperacionProductoBinding
    private var idProducto: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOperacionProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Leer los argumentos... si existen argumentos, entonces mostrar en las cajas de texto
        if (arguments != null) {
            // ?: si esta nulo o vacio
            idProducto = arguments?.getInt("id") ?: 0
            binding.etCodigoBarra.setText(arguments?.getString("codigobarra"))
            binding.etDescripcion.setText(arguments?.getString("descripcion"))
            binding.etCosto.setText(arguments?.getDouble("costo").toString())
            binding.etPrecio.setText(arguments?.getDouble("precio").toString())
            binding.etStock.setText(arguments?.getInt("stock").toString())
        }

        //codigo
        binding.fabGrabar.setOnClickListener {
            UtilsCommon.ocultarTeclado(requireContext(), it)

            if (binding.etCodigoBarra.text.toString().trim().isEmpty() ||
                binding.etDescripcion.text.toString().trim().isEmpty() ||
                binding.etCosto.text.toString().trim().isEmpty() ||
                binding.etPrecio.text.toString().trim().isEmpty() ||
                binding.etStock.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Todos los campos son obligatorios",
                    Toast.LENGTH_LONG
                ).show()

                // Detiene la accion
                return@setOnClickListener
            }

            val entidad = Producto()
            entidad.descripcion = binding.etDescripcion.text.toString().trim()
            entidad.codigoBarra = binding.etCodigoBarra.text.toString().trim()
            entidad.costo = binding.etCosto.text.toString().toDouble()
            entidad.precio = binding.etPrecio.text.toString().toDouble()
            entidad.stock = binding.etStock.text.toString().toInt()

            if (idProducto == 0)
                grabar(entidad)
            else {
                entidad.id = idProducto
                actualizar(entidad)
            }
        }
    }

    // Limpiar los valores de las cajas de texto
    private fun limpiarDatos() {
        // Limpiar las cajas de texto
        UtilsCommon.limpiarEditText(requireView())
        idProducto = 0

        // Asignar el cursor en codigo de barra
        binding.etCodigoBarra.requestFocus()
    }

    // Metodo para grabar
    private fun grabar(entidad: Producto) {
        var msgError = ""

        lifecycleScope.launch {
            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    ProductoDao.grabar(entidad)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    0
                }
            }

            if (msgError.trim().isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
            } else {
                if (result > 0) {
                    // Si logro eliminar el registro
                    Toast.makeText(
                        requireContext(),
                        "El nuevo registro fue grabado correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    limpiarDatos()
                }
            }

        }
    }

    private fun actualizar(entidad: Producto) {
        var msgError = ""

        lifecycleScope.launch {
            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    ProductoDao.actualizar(entidad)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    0
                }
            }

            if (msgError.trim().isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
            } else {
                if (result > 0) {
                    // Si logro actualizar el registro
                    Toast.makeText(
                        requireContext(),
                        "El registro fue grabado correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    limpiarDatos()
                }
            }

        }
    }

}