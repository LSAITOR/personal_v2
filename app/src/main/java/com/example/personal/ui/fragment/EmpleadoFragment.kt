package com.example.personal.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal.R
import com.example.personal.data.dao.EmpleadoDao
import com.example.personal.data.model.Emplea2
import com.example.personal.databinding.FragmentEmpleadoBinding
import com.example.personal.ui.adapter.EmpleadoAdapter
import com.example.personal.ui.communicator.EmpleadoComunicador
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// EmpleadoAdapter.IOnItemCLickListener --> Implenttacion de la interfaz del adaptador
class EmpleadoFragment : Fragment(), EmpleadoAdapter.IOnItemCLickListener {

    // ENlazar la interfaz grafica (siempre - manual)
    private lateinit var binding: FragmentEmpleadoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Realizar siempre (manual)
        binding = FragmentEmpleadoBinding.inflate(inflater)
        return binding.root
    }

    // Agregar siempre (manual)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Siempre realizar el enlace ( vista de item en forma de lista)
        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        // Enlazar el  adaptador con el rvlista
        binding.rvLista.adapter = EmpleadoAdapter(this)

        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // Envia el texto ingresado en la caja, hacia el metodo listarEmpleado
                listarEmpleado(p0.toString().trim())
            }

        })

        binding.fabNuevo.setOnClickListener {
            // Asignar un valor nulo al comunicador empleado
            EmpleadoComunicador.entidad = null
            // Enviar al fragmento Agregar empleado
            Navigation.findNavController(requireView()).navigate(R.id.action_nav_empleado_to_agregarEmpleadoFragment)
        }

        // Esto se va a ejecutar la primera vez que carga el fragmento
        listarEmpleado("")
    }

    private fun listarEmpleado(dato: String) {
        var msgError = ""

        lifecycleScope.launch {

            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    EmpleadoDao.leerEmplea2(dato)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    null
                }
            }

            if (msgError.trim().isNotEmpty()) {
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()
            } else {
                // result -> contiene la lista de empleado
                (binding.rvLista.adapter as EmpleadoAdapter).setData(result!!)
            }

        }
    }

    private fun eliminar(entidad: Emplea2) {
        var msgError = ""

        lifecycleScope.launch {

            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    EmpleadoDao.eliminarEmplea2(entidad)
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
                    Toast.makeText(requireContext(), "El registro fue eliminado", Toast.LENGTH_LONG).show()
                    // Actualizar la lista (recyclerview)
                    listarEmpleado(binding.etBuscar.text.toString().trim())
                }
            }

        }
    }

    override fun clickEditar(entidad: Emplea2) {
        //Toast.makeText(requireContext(), entidad.nombre, Toast.LENGTH_LONG).show()
        EmpleadoComunicador.entidad = entidad
        // Enviar al fragmento operacionempleadofragment
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_empleado_to_operacionEmpleadoFragment)
    }

    override fun clickEliminar(entidad: Emplea2) {
        //Toast.makeText(requireContext(), entidad.ine, Toast.LENGTH_LONG).show()
        // Alerta
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("ELIMINAR")
            setMessage("¿Desea eliminar el registro: ${entidad.nombre}?")
            setCancelable(false)

            setPositiveButton("SI") { dialogo, _ ->
                // Debe llamar a la funcion de eliminar
                eliminar(entidad)
                // desaparecer el mensaje
                dialogo.dismiss()
            }

            setNegativeButton("NO") { dialogo, _ ->
                dialogo.cancel()
            }
        }.create().show()
    }

}