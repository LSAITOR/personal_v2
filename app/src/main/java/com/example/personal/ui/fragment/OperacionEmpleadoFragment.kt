package com.example.personal.ui.fragment

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.dao.EmpleadoDao
import com.example.personal.data.model.Emplea2
import com.example.personal.databinding.FragmentOperacionEmpleadoBinding
import com.example.personal.ui.communicator.EmpleadoComunicador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OperacionEmpleadoFragment : Fragment() {

    private lateinit var binding: FragmentOperacionEmpleadoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOperacionEmpleadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(EmpleadoComunicador.entidad != null) {
            binding.etIne.setText(EmpleadoComunicador.entidad?.ine)
            binding.etNombre.setText(EmpleadoComunicador.entidad?.nombre)
            binding.etDireccion.setText(EmpleadoComunicador.entidad?.direccion)
            binding.etCelular.setText(EmpleadoComunicador.entidad?.celular)
            binding.etSueldoDiario.setText(EmpleadoComunicador.entidad?.sueldodia.toString())

            // Asigna el tipo de entrada a nulo de etIne
            // No se podra escribir en ese control
            binding.etIne.inputType = InputType.TYPE_NULL
        }

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.ocultarTeclado(requireContext(), requireView())

            // Verificar que las cajas de texto tengan informacion
            if(binding.etIne.text.toString().trim().isEmpty() ||
                binding.etNombre.text.toString().trim().isEmpty()) {
                // Mostrar mensaje
                Toast.makeText(requireContext(), "Faltan datos requeridos", Toast.LENGTH_LONG).show()
                // Abandonar la accion
                return@setOnClickListener
            }

            val entidad = Emplea2().apply {
                ine = EmpleadoComunicador.entidad?.ine.toString()
                nombre = binding.etNombre.text.toString().trim()
                direccion = binding.etDireccion.text.toString().trim()
                celular = binding.etCelular.text.toString().trim()
                sueldodia = binding.etSueldoDiario.text.toString().trim().toDouble()
            }

            actualizar(entidad)
            //grabar(entidad)
        }
    }

    private fun actualizar(entidad: Emplea2) {
        var msgError = ""

        lifecycleScope.launch {

            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    EmpleadoDao.actualizar(entidad)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    0
                }
            }

            if (msgError.trim().isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
            } else {
                if(result > 0) {
                    // Si logro eliminar el registro
                    Toast.makeText(requireContext(), "El registro fue actualizado correctamente", Toast.LENGTH_LONG).show()
                    // Limpiar las cajas de texto
                    UtilsCommon.limpiarEditText(requireView())
                }
            }

        }
    }

    private fun grabar(entidad: Emplea2) {
        var msgError = ""

        lifecycleScope.launch {

            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    EmpleadoDao.grabar(entidad)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    0
                }
            }

            if (msgError.trim().isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
            } else {
                if(result > 0) {
                    // Si logro registrar el registro
                    Toast.makeText(requireContext(), "El nuevo registro fue grabado correctamente", Toast.LENGTH_LONG).show()
                    // Limpiar las cajas de texto
                    binding.etIne.setText("")
                    binding.etNombre.setText("")
                    binding.etDireccion.setText("")
                    binding.etCelular.setText("")
                    binding.etSueldoDiario.setText("")
                }
            }

        }
    }
}