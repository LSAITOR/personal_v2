package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.personal.data.dao.EmpleadoDao
import com.example.personal.databinding.FragmentBuscarIneBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuscarIneFragment : Fragment() {

    // CRear binding
    private lateinit var binding: FragmentBuscarIneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // asignamos valor al binding
        binding = FragmentBuscarIneBinding.inflate(inflater)
        // Devuelve la raiz del binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btBuscar.setOnClickListener {

            if (binding.etBuscarIne.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Debe digitar un INE a buscar", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            var msgError = ""

            // Corrutina
            lifecycleScope.launch {

                // variable inmutable, que recibe un resultado de la db
                val result = withContext(Dispatchers.IO) {
                    try {
                        EmpleadoDao.obtenerEmplea2(binding.etBuscarIne.text.toString().trim())
                    } catch (e: Exception) {
                        msgError = e.message.toString()
                        null
                    }
                }

                if (msgError.trim().isNotEmpty()) {
                    Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
                } else {
                    binding.etNombre.setText(result?.nombre)
                    binding.etDireccion.setText(result?.direccion)
                    binding.etCelular.setText(result?.celular)
                    binding.etSueldoDiario.setText(result?.sueldodia.toString())

                    //0000limpiarTexto()
                }

            }
            binding.btnlimpiar.setOnClickListener() {
                btnlimpiar()}


        }
    }

    //private fun limpiarTexto() {
    // binding.etBuscarIne.setText("")
    //binding.etBuscarIne.requestFocus()}

    private fun btnlimpiar() {
        binding.etBuscarIne.setText("")
        binding.etNombre.setText("")
        binding.etDireccion.setText("")
        binding.etCelular.setText("")
        binding.etSueldoDiario.setText("")
        binding.etBuscarIne.requestFocus()
    }

}