package com.example.personal.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal.R
import com.example.personal.data.dao.ProductoDao
import com.example.personal.data.model.Producto
import com.example.personal.databinding.FragmentProductoBinding
import com.example.personal.ui.adapter.ProductoAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductoFragment : Fragment(), ProductoAdapter.IOnItemCLickListener {

    //1. crear una promesa (lateinit) del binding
    private lateinit var binding: FragmentProductoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 2. Dar valor a la variable binding
        binding = FragmentProductoBinding.inflate(inflater, container, false)
        // 3. Retornar la raiz del binding (devolvera a todos los objetos que contenga - botones, cajas de texto, etc)
        return binding.root
    }

    // 4. crear onViewCreated (esto ocurre despues de que se haya creado la vista)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //5.  Siempre realizar el enlace ( vista de item en forma de lista)
        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        // Enlazar el  adaptador con el rvlista
        binding.rvLista.adapter = ProductoAdapter(this)

        // Buscar en la caja de texto.
        binding.etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                // Envia el texto ingresado en la caja, hacia el metodo listarEmpleado
                listar(binding.etBuscar.text.toString().trim())
            }

        })

        binding.fabNuevo.setOnClickListener {
            // Nos envia al fragmento operacion producto
            Navigation.findNavController(it).navigate(R.id.action_nav_producto_to_operacionProductoFragment)
        }

        // Realiza la carga inicial de datos
        listar("")
    }

    private fun listar(dato: String) {
        var msgError = ""

        // lanzar la corrutina
        lifecycleScope.launch {
            // indicar el ambito de la corrutina (Dispatchers.IO se utiliza para acceder a base de datos
            // archivos locales o externos, api rest)
            // Main... hace referencia al hilo principal (de preferencia no usar)
            val result = withContext(Dispatchers.IO) {
                // proteger de cualquier error
                try {
                    ProductoDao.listarProducto(dato)
                } catch (e: Exception) {
                    msgError=e.message.toString()
                    null
                }
            }

            // Salio del hilo secundario
            if(msgError.isNotEmpty())
                Toast.makeText(
                    requireContext(), msgError, Toast.LENGTH_LONG
                ).show()

            // Mostrar el resultado en el rvLista
            (binding.rvLista.adapter as ProductoAdapter).setData(result!!)
        }
    }

    override fun clickEditar(entidad: Producto) {
        //  Pair("NombreQueIdentifica", Valor que se enviara)
        // bundleOf() -> Enviar argumentos (uno o varios)
        Navigation.findNavController(requireView()).navigate(
            R.id.action_nav_producto_to_operacionProductoFragment,
            bundleOf(
                Pair("id", entidad.id),
                Pair("codigobarra", entidad.codigoBarra),
                Pair("descripcion", entidad.descripcion),
                Pair("costo", entidad.costo),
                Pair("precio", entidad.precio),
                Pair("stock", entidad.stock)
            )
        )
    }

    override fun clickEliminar(entidad: Producto) {
// Alerta
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("ELIMINAR")
            setMessage("¿Desea eliminar el registro: ${entidad.descripcion}?")
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

    private fun eliminar(entidad: Producto) {
        var msgError = ""

        lifecycleScope.launch {
            // variable inmutable, que recibe un resultado de la db
            val result = withContext(Dispatchers.IO) {
                try {
                    // Retorna la lista de empleados
                    ProductoDao.eliminar(entidad)
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
                    listar(binding.etBuscar.text.toString().trim())
                }
            }

        }
    }
}