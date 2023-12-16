package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.model.DetalleTicket
import com.example.personal.data.model.Ticket
import com.example.personal.databinding.ItemsDetalleTicketBinding
import com.example.personal.databinding.ItemsReporteTicketBinding

// Este adaptador implemeta una interfaz, la actividad o fragmente que lo use estara
// obligado a implementar estas interfaces
class ReporteTicketAdapter(
    private val iOnClickListener: IOnClickListener
): RecyclerView.Adapter<ReporteTicketAdapter.BindViewHolder>() {

    // Lista de productos en el ticket
    private var lista: List<Ticket> = emptyList()

    // Crear interfaces para los botones de anular e ir al detalle
    // Las interfaces no implementa acciones. Las acciones seran implementadas en
    // el fragmento o actividad de destino
    interface IOnClickListener {
        fun clickAnular(entidad: Ticket)
        fun clickDetalle(entidad: Ticket)
    }

    inner class BindViewHolder(vista: View): RecyclerView.ViewHolder(vista) {
        // Variable
        val binding = ItemsReporteTicketBinding.bind(vista)

        // Funcion para enlazar la vista con los datos (base de datos - tablas)
        fun enlazar(entidad: Ticket) {

            binding.tvTitulo.text = "Ticket ${entidad.id.toString()}"
            binding.tvFecha.text = entidad.fecha.toString()
            binding.tvCliente.text = entidad.cliente
            binding.tvEstado.text = entidad.estado
            binding.tvTotal.text = UtilsCommon.formatearDoubleString(entidad.total)

            // Asignar la interfaz a los botones
            binding.ibAnular.setOnClickListener { iOnClickListener.clickAnular(entidad) }
            binding.ibAgregar.setOnClickListener { iOnClickListener.clickDetalle(entidad) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        // Asociar la vista
        return BindViewHolder(
            // Damos la vista
            LayoutInflater.from(parent.context).inflate(R.layout.items_reporte_ticket, parent, false  )
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

    fun setData(_lista: List<Ticket>) {
        // Lenamos la lista interna con lo que viene de la consulta sql (_lista)
        this.lista = _lista
        // notificar los cambios
        notifyDataSetChanged()
    }
}