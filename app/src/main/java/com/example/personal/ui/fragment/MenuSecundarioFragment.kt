package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.personal.R
import com.example.personal.databinding.FragmentMenuSecundarioBinding


class MenuSecundarioFragment : Fragment() {

    // Crear el binding
    private lateinit var binding: FragmentMenuSecundarioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Asignar valor al binding
        binding = FragmentMenuSecundarioBinding.inflate(inflater, container, false)
        // Retona la raiz del binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mcvRegistrarVenta.setOnClickListener {
            //Toast.makeText(requireContext(), "Hola saito", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_nav_menu_secundario_to_nav_home)
        }

        binding.mcvProducto.setOnClickListener {
            findNavController().navigate(R.id.action_nav_menu_secundario_to_nav_producto)
        }

        binding.mcvEmpleado.setOnClickListener {
            findNavController().navigate(R.id.action_nav_menu_secundario_to_nav_empleado)
        }

        binding.mcvCliente.setOnClickListener {
            findNavController().navigate(R.id.action_nav_menu_secundario_to_nav_cliente)
        }

        binding.mcvReporteTicket.setOnClickListener {
            findNavController().navigate(R.id.action_nav_menu_secundario_to_nav_reporte_ticket)
        }

    }


}