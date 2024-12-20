package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.core.UtilsMessage
import com.example.personal.data.dao.ClienteDao
import com.example.personal.data.model.Cliente
import com.example.personal.databinding.FragmentOperacionClienteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OperacionClienteFragment : Fragment() {

    private lateinit var binding: FragmentOperacionClienteBinding
    private var idcliente = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOperacionClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         Recibir datos del cliente existente para actualizar
        if (arguments != null) {
            // ?: si esta nulo o vacio
            idcliente = arguments?.getInt("id") ?: 0
            binding.etNombre.setText(arguments?.getString("nombre"))
            binding.etRfc.setText(arguments?.getString("rfc"))
            binding.etDireccion.setText(arguments?.getString("direccion"))
            binding.etEmail.setText(arguments?.getString("email"))
            binding.swEstado.isChecked = arguments?.getInt("estatus") == 1
        }


        binding.fabGrabar.setOnClickListener {
            // Verificar que el nombre y el rfc existan (obligatorios)
            if(binding.etRfc.text.toString().trim().isEmpty() || binding.etNombre.text.toString().trim().isEmpty()){
                UtilsMessage.showAlert(
                    "Advertencia", "el nombre y el rfc son obligatorios", requireContext()
                )
                return@setOnClickListener
            }

            // Crea elobjeto cliente
            val cliente = Cliente()

            cliente.id = idcliente
            // Asignar valores a sus propiedades (cliente)
            cliente.nombre = binding.etNombre.text.toString().trim()
            cliente.rfc = binding.etRfc.text.toString().trim()
            cliente.direccion = binding.etDireccion.text.toString().trim()
            cliente.email = binding.etEmail.text.toString().trim()
            cliente.estatus = if(binding.swEstado.isChecked) 1 else 0

            grabar(cliente)
        }
    }

    private fun grabar(cliente: Cliente) {
        var msgError = ""

        // ejecutar una corrutina
        lifecycleScope.launch {
            binding.progressBar.isVisible = true

            //Ejecutar la corrutina en el hilo secundario
            withContext(Dispatchers.IO) {
                try {
                    // Si el ID del cliente es Cero (0) entonces vamos a registrar un nuevo cliente
                    if(cliente.id ==0)
                        ClienteDao.registrar(cliente)
                    else // Caso contrario vamos a actualizar los datos del cliente existente
                        ClienteDao.actualizar(cliente)
                } catch (e: Exception) {
                    msgError = e.message.orEmpty()
                }
            }

            binding.progressBar.isVisible = false

            // verificar si hubo error al ejecutar el hilo secundario
            if (msgError.isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
                return@launch
            }

            // Asiganamos el valor cero a nuestra variable (reset)
            idcliente =0
            // Limpiar los edittext
            UtilsCommon.limpiarEditText(requireView())
            // Asignar el foco a nuestro edittext nombre
            binding.etNombre.requestFocus()
            // Muestra el mensaje
            Toast.makeText(requireContext(), "Datos grabados", Toast.LENGTH_LONG).show()

        }
    }

}
