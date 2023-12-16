package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.model.DetalleTicket
import com.example.personal.databinding.ItemsReporteDetalleBinding

class ReporteDetalleTicketAdapter: RecyclerView.Adapter<ReporteDetalleTicketAdapter.BindViewHolder>() {

    // Lista de productos en el detalle ticket e iniciara vacia
    private var lista: List<DetalleTicket> = emptyList()

    inner class BindViewHolder(vista: View): RecyclerView.ViewHolder(vista) {
        // Variable
        val binding = ItemsReporteDetalleBinding.bind(vista)

        // Funcion para enlazar la vista con los datos (base de datos - tablas)
        fun enlazar(entidad: DetalleTicket) {

            binding.tvDetalle.text = entidad.mproducto?.descripcion
            binding.tvCodigoBarra.text = entidad.mproducto?.codigoBarra
            binding.tvPrecio.text = UtilsCommon.formatearDoubleString(entidad.precio)
            binding.tvCantidad.text = entidad.cantidad.toString()
            binding.tvImporte.text = UtilsCommon.formatearDoubleString(entidad.importe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        // Asociar la vista
        return BindViewHolder(
            // Damos la vista
            LayoutInflater.from(parent.context).inflate(R.layout.items_reporte_detalle, parent, false  )
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