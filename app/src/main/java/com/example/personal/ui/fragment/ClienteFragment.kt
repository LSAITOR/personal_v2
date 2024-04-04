package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personal.R
import com.example.personal.databinding.FragmentClienteBinding


class ClienteFragment : Fragment() {

    // Crear una promesa (lateinit var) para el binding, antes de ser usado debera ser creado
    private lateinit var binding: FragmentClienteBinding

    // Funcion crea la vista
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Cumplir la promesa, asignando un valor al binding
        binding = FragmentClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Despues de que se ha creado la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.
    }


}