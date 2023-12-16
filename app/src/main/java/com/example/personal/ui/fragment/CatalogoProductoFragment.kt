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
import com.example.personal.data.dao.ProductoDao
import com.example.personal.data.model.DetalleTicket
import com.example.personal.data.model.Producto
import com.example.personal.databinding.FragmentCatalogoProductoBinding
import com.example.personal.ui.activity.MainActivity
import com.example.personal.ui.adapter.CatalogoAdapter
import com.example.personal.ui.dialog.CantidadDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatalogoProductoFragment : Fragment(), CatalogoAdapter.IOnItemCLickListener,
    CantidadDialog.IEnviarListener {

    private lateinit var binding: FragmentCatalogoProductoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatalogoProductoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        binding.rvLista.adapter = CatalogoAdapter(this)

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

        binding.fabCarrito.setOnClickListener {
            // Verificar que la lista carrita tenga elementos
            // para poder navergar a la siguiente pantalla
            if(MainActivity.detalleCarrito.isEmpty()){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("ADVERTENCIA")
                    .setMessage("El carrito esta vacio, imposible mostrar datos")
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()

                // Abandonar el proceso
                return@setOnClickListener
            }

            // Navega hacia el fragmento registrar ticket
            Navigation.findNavController(it).navigate(R.id.action_nav_home_to_registrarTicketFragment)
        }

        // Realiza la carga inicial de datos
        listar("")
        datoInicial()
    }

    // Creando variables estaticas (companion object)
    companion object {
        // Creando una varible estatica de tipo Producto y que esta puede ser nulo
        var productoActual: Producto? = null
    }

    private fun datoInicial() {
        binding.fabCarrito.setText("Carrito [${MainActivity.calcular()}]")
    }

    override fun clickAgregarItem(entidad: Producto) {
        //Toast.makeText(requireContext(), entidad.descripcion,Toast.LENGTH_LONG).show()

        // Mostrar un simple dialogo del sistema android
        /*MaterialAlertDialogBuilder(requireContext())
            .setMessage(entidad.descripcion)
            .setTitle("Producto")
            .setCancelable(false)
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()*/

        productoActual = entidad

        CantidadDialog.newInstance(
            entidad.descripcion, entidad.precio, this
        ).show(parentFragmentManager, "Cantidad")
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
                    msgError = e.message.toString()
                    null
                }
            }

            // Salio del hilo secundario
            if (msgError.isNotEmpty())
                Toast.makeText(
                    requireContext(), msgError, Toast.LENGTH_LONG
                ).show()

            // Mostrar el resultado en el rvLista
            (binding.rvLista.adapter as CatalogoAdapter).setData(result!!)
        }
    }

    override fun enviarItem(cantidad: Int, precio: Double) {
        if (cantidad == 0 || precio == 0.0) {
            productoActual = null
            return // Abandona el proceso
        }

        // Creamos el objeto DetalleTicket
        val entidad = DetalleTicket()
        entidad.mproducto = productoActual
        entidad.precio = precio
        entidad.cantidad= cantidad
        entidad.importe = cantidad * precio

        // Agregar el detalleticket a nuestra lista detalleCarrito
        MainActivity.detalleCarrito.add(entidad)

        // Mostrar la cantidad de producto agregado a la lista
        binding.fabCarrito.setText("Carrito [${MainActivity.calcular()}]")

        /*MaterialAlertDialogBuilder(requireContext())
            .setMessage("Cantidad: ${cantidad.toString()}")
            .setTitle("Precio: ${precio.toString()}")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()*/

    }

}