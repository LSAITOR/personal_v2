package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.R
import com.example.personal.data.model.Emplea2
import com.example.personal.databinding.ItemsEmpleadoBinding

class EmpleadoAdapter(
    // Un parametro que se envia por constructor
    private val iOnItemCLickListener: IOnItemCLickListener
): RecyclerView.Adapter<EmpleadoAdapter.BindViewHolder>() {

    // Creando una lista vacia de tipo Emplea2
    // emptyList -> LIsta vacia
    // lista: List<Emplea2> --> lista de tipo Emplea2
    // Siempre lo crearemos
    private var lista: List<Emplea2> = emptyList()

    // Crear una interfaz (comunicar datos) (Se creara segun sea el caso)
    interface IOnItemCLickListener {
        // Sirve para interactuar con el boton de editar y eliminar de la lista
        fun clickEditar(entidad: Emplea2)
        fun clickEliminar(entidad: Emplea2)
    }

    // Siempre lo vamos a crear
    inner class BindViewHolder(vista: View): RecyclerView.ViewHolder(vista) {
        // Variable
        val binding = ItemsEmpleadoBinding.bind(vista)

        // Funcion para enlazar la vista con los datos (base de datos - tablas)
        fun enlazar(entidad: Emplea2) {

            binding.tvNombre.text = entidad.nombre
            binding.tvIne.text = entidad.ine
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        // Asociar la vista
        return BindViewHolder(
            // Damos la vista
        LayoutInflater.from(parent.context).inflate(R.layout.items_empleado, parent, false  )
        )
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        // Obtener los datos
        val item = lista[position]

        // Pasar cada item al binding (vista - diseño)
        holder.enlazar(item)

        // Asociar las acciones de los botones
        holder.binding.ibEditar.setOnClickListener { iOnItemCLickListener.clickEditar(item) }
        holder.binding.ibEliminar.setOnClickListener { iOnItemCLickListener.clickEliminar(item) }
    }

    override fun getItemCount(): Int {
        // Debe de retornar el numro total de la lista
        return lista.size
    }

    // Siempre lo crearemos0
    fun setData(_lista: List<Emplea2>) {
        this.lista = _lista
        // notificar los cambios
        notifyDataSetChanged()
    }
}