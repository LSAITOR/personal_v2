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
import com.example.personal.data.dao.ClienteDao
import com.example.personal.data.model.Cliente
import com.example.personal.databinding.FragmentOperacionClienteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OperacionClienteFragment : Fragment() {

    private lateinit var binding: FragmentOperacionClienteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOperacionClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun grabar(cliente: Cliente){
        var msgError=""

        // ejecutar una corrutina
        lifecycleScope.launch {
            binding.progressBar.isVisible=true

            //Ejecutar la corrutina en el hilo secundario
            withContext(Dispatchers.IO){
                try {
                    ClienteDao.registrar(cliente)
                } catch (e: Exception){
                    msgError=e.message.orEmpty()
                }
            }

            binding.progressBar.isVisible=false

            // verificar si hubo error al ejecutar el hilo secundario
            if(msgError.isNotEmpty()){
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
                return@launch
            }

            Toast.makeText(requireContext(), "Datos grabados", Toast.LENGTH_LONG).show()
            
        }
    }

}
