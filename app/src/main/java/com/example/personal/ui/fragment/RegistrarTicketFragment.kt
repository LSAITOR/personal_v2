package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.dao.TicketDao
import com.example.personal.data.model.DetalleTicket
import com.example.personal.data.model.Ticket
import com.example.personal.databinding.FragmentRegistrarTicketBinding
import com.example.personal.ui.activity.MainActivity
import com.example.personal.ui.adapter.DetalleTicketAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.Calendar

class RegistrarTicketFragment : Fragment(), DetalleTicketAdapter.IOnClickListerner {

    private lateinit var binding: FragmentRegistrarTicketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrarTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())
        // Agregar un decorador lineal
        binding.rvLista.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        binding.rvLista.adapter = DetalleTicketAdapter(this)

        binding.fabGrabar.setOnClickListener {
            // Verificar si el carrito tiene elementos
            if(MainActivity.detalleCarrito.size==0)
                return@setOnClickListener

            // Crear el objeto ticket
            val entidad = Ticket()
            // asignar valores al objeto entidad
            entidad.cliente = binding.etCliente.text.toString().trim()
            entidad.estado = "Vigente"
            entidad.fecha = Date(Calendar.getInstance().time.time)
            entidad.total = MainActivity.calcularTotal()
            entidad.detalle = MainActivity.detalleCarrito

            grabar(entidad)
        }

        // Mostrar los elementos del carrito en la lista (recyclerview)
        leerCarrito()
    }

    private fun leerCarrito(){
        // Mostrar los elementos del carrito en la lista (recyclerview)
        (binding.rvLista.adapter as DetalleTicketAdapter).setData(MainActivity.detalleCarrito)

        // Mostrar la suma total
        // El simbolo $ se usa para concatenar
        binding.tvTotal.text = "TOTAL: ${UtilsCommon.formatearDoubleString(MainActivity.calcularTotalImporte())}"
    }

    private fun grabar(entidad: Ticket) {
        var msgError = ""
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    TicketDao.grabar(entidad)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    0
                }
            }

            if (msgError.isNotEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle("ERROR")
                    .setMessage(msgError)
                    .setPositiveButton("Aceptar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
                return@launch
            }

            // Limpiar el carrito
            MainActivity.limpiarListaCarrito()
            binding.etCliente.setText("")

            // Mostrar mensaje de exito
            Toast.makeText(requireContext(), "Ticket grabado con exito", Toast.LENGTH_LONG).show()

            // Volver al catalogo de producto
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

    override fun clickQuitar(entidad: DetalleTicket) {
        // Implementar la accion del boton quitar
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setCancelable(false)
        builder.setMessage("¿Esta seguro que desea quitar el elemento: ${entidad.mproducto?.descripcion} de la lista?")
        builder.setPositiveButton("SI") { dialog, _ ->
            // Buscar el item (entidad) de la lista y si lo encuantra, quitarlo. Actualizar vista que se refleja en el fragmento

            // Usar un bucle (for) para recorrer sus elementos
            for (item in MainActivity.detalleCarrito){
                android.util.Log.e("ITEM", item.mproducto?.descripcion.toString())
                // Buscar el item y si lo encuantra, quitarlo de la lista
                // Si es verdad, entonces quitar el elemento
                if(MainActivity.detalleCarrito.contains(entidad)) {
                    // Quitar el elemento encontrado (remove)
                    MainActivity.detalleCarrito.remove(entidad)
                    // Salir del bucle
                    break
                }
            }

            // Verificar si la lista aun tiene items
            // size -> muesra el numero de items
            if(MainActivity.detalleCarrito.size==0){
                // Si no existe items, entonces volver a la pantalla anterior (popBackStack())
                Navigation.findNavController(requireView()).popBackStack()
            }

            // Actualizar la vista del carrito
            leerCarrito()
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            // Cerrar el dialogo
            dialog.cancel()
        }
        // Construir el dialogo
        builder.create()
        //luego mostrarlo
        builder.show()
    }

    override fun clickRestar(entidad: DetalleTicket) {
        // Recorrer los items de la lista y hacer uso del forEach
        MainActivity.detalleCarrito.forEach { item ->
            // AND logico -> && .... ambos operadores deben ser verdadero para que el resultado sea verdad
            // OR logico -> ||
            // Diferente -> !=

            // Verificar si el codigo encontrado es igual a entidad
            if(item.mproducto?.id == entidad.mproducto?.id && item.cantidad > 1) {
                // Dosminuir la cantidad del producto pedido
                item.cantidad--
                // Actualizar el valor del importe
                item.importe = item.cantidad * item.precio
                // Informar el cambio que a ocurrido en la lista y mostrar los datos en la vista (rvlista)
                leerCarrito()
            }
        }
    }

    override fun clickSumar(entidad: DetalleTicket) {
        // Recorrer la lista del carrito
        MainActivity.detalleCarrito.forEach {
            // Verificar el codigo del item a modificar su cantidad
            if(it.mproducto?.id == entidad.mproducto?.id) {
                // Aumentar la cantidad del producto pedido
                it.cantidad++
                // Actualizar el valor del importe
                it.importe = it.cantidad * it.precio
                // Informar el cambio que a ocurrido en la lista y mostrar los datos en la vista (rvlista)
                leerCarrito()
            }
        }
    }


}