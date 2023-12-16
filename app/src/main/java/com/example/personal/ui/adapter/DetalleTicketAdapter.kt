package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.model.DetalleTicket
import com.example.personal.databinding.ItemsDetalleTicketBinding

// Aquellos que usen el DetalleTicketAdapter estará obligado a implementar la interfaz IOnClickListerner
class DetalleTicketAdapter(
    // Constructor
    private val iOnClickListerner: IOnClickListerner
): RecyclerView.Adapter<DetalleTicketAdapter.BindViewHolder>() {

    // Lista de productos en el detalle ticket
    private var lista: List<DetalleTicket> = emptyList()

    // Crear las interfaces para quitar un producto de la lista, aumentar o disminuir
    interface IOnClickListerner {
        fun clickQuitar(entidad: DetalleTicket)
        fun clickRestar(entidad: DetalleTicket)
        fun clickSumar(entidad: DetalleTicket)
    }

    inner class BindViewHolder(vista: View): RecyclerView.ViewHolder(vista) {
        // Variable
        val binding = ItemsDetalleTicketBinding.bind(vista)

        // Funcion para enlazar la vista con los datos (base de datos - tablas)
        fun enlazar(entidad: DetalleTicket) {

            binding.tvDetalle.text = entidad.mproducto?.descripcion
            binding.tvCodigoBarra.text = entidad.mproducto?.codigoBarra
            binding.tvPrecio.text = UtilsCommon.formatearDoubleString(entidad.precio)
            binding.tvCantidad.text = entidad.cantidad.toString()
            binding.tvImporte.text = UtilsCommon.formatearDoubleString(entidad.importe)

            //Boton quitar
            binding.ibQuitar.setOnClickListener { iOnClickListerner.clickQuitar(entidad)}
            binding.ibRestar.setOnClickListener { iOnClickListerner.clickRestar(entidad) }
            binding.ibSumar.setOnClickListener { iOnClickListerner.clickSumar(entidad) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        // Asociar la vista
        return BindViewHolder(
            // Damos la vista
            LayoutInflater.from(parent.context).inflate(R.layout.items_detalle_ticket, parent, false  )
        )
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        // Obtener los datos
        val item = lista[position]

        // Pasar cada item al binding (vista - diseño)
        holder.enlazar(item)
    }

    fun setData(_lista: List<DetalleTicket>) {
        // Lenamos la lista interna con lo que viene de la consulta sql (_lista)
        this.lista = _lista
        // notificar los cambios
        notifyDataSetChanged()
    }
}