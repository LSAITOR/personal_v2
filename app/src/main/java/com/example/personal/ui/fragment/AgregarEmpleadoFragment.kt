package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.personal.core.UtilsCommon
import com.example.personal.data.dao.EmpleadoDao
import com.example.personal.data.model.Emplea2
import com.example.personal.databinding.FragmentAgregarEmpleadoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarEmpleadoFragment : Fragment() {

    // Creando una promesa (lateinit) del binding
    private lateinit var binding: FragmentAgregarEmpleadoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Damos valor al binding
        binding = FragmentAgregarEmpleadoBinding.inflate(inflater, container, false)
        // Retorna la raiz del binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabGrabar.setOnClickListener {
            UtilsCommon.ocultarTeclado(requireContext(), requireView())

            if(binding.etIne.text.toString().trim().isEmpty() ||
                binding.etNombre.text.toString().trim().isEmpty() ||
                binding.etDireccion.text.toString().trim().isEmpty() ||
                binding.etCelular.text.toString().trim().isEmpty() ||
                binding.etSueldoDiario.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Todos los datos son obligatorios",
                    Toast.LENGTH_LONG).show()
                // Abandona el proceso o la ejecucion
                return@setOnClickListener
            }

            val entidad = Emplea2()
            entidad.ine = binding.etIne.text.toString().trim()
            entidad.nombre = binding.etNombre.text.toString().trim()
            entidad.direccion = binding.etDireccion.text.toString().trim()
            entidad.celular = binding.etCelular.text.toString().trim()
            entidad.sueldodia = binding.etSueldoDiario.text.toString().trim().toDouble()


            grabar(entidad)
        }
    }

    // Funcion privada
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

            // Verificar si existe dato en la variable msgError
            if (msgError.trim().isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
            } else {
                // Verifica que la operacion haya sido exitosa
                if(result > 0) {
                    // Si logro eliminar el registro
                    Toast.makeText(requireContext(), "El registro fue agregado correctamente", Toast.LENGTH_LONG).show()

                    UtilsCommon.limpiarEditText(requireView())
                }
            }

        }
    }
}