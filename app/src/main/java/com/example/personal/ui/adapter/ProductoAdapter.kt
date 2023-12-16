package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.model.Producto
import com.example.personal.databinding.ItemsProductoBinding

class ProductoAdapter(
    // Un parametro que se envia por constructor
    private val iOnItemCLickListener: IOnItemCLickListener
): RecyclerView.Adapter<ProductoAdapter.BindViewHolder>() {

    // Creando una lista vacia de tipo Emplea2
    // emptyList -> LIsta vacia
    // lista: List<Emplea2> --> lista de tipo Emplea2
    // Siempre lo crearemos
    private var lista: List<Producto> = emptyList()

    // Crear una interfaz (comunicar datos) (Se creara segun sea el caso)
    interface IOnItemCLickListener {
        // Sirve para interactuar con el boton de editar y eliminar de la lista
        // Las interfaces se van a implementar en las clases de destino o donde se usaran (si o si se deben de implementar en la calse de destino)
        fun clickEditar(entidad: Producto)
        fun clickEliminar(entidad: Producto)
    }

    // Siempre lo vamos a crear
    inner class BindViewHolder(vista: View): RecyclerView.ViewHolder(vista) {
        // Variable
        val binding = ItemsProductoBinding.bind(vista)

        // Funcion para enlazar la vista con los datos (base de datos - tablas)
        fun enlazar(entidad: Producto) {

            binding.tvDescripcion.text = entidad.descripcion
            binding.tvCodigoBarra.text = entidad.codigoBarra
            binding.tvCosto.text = entidad.costo.toString()
            binding.tvPrecio.text = UtilsCommon.formatearDoubleString(entidad.precio)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        // Asociar la vista
        return BindViewHolder(
            // Damos la vista
        LayoutInflater.from(parent.context).inflate(R.layout.items_producto, parent, false  )
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
    fun setData(_lista: List<Producto>) {
        this.lista = _lista
        // notificar los cambios
        notifyDataSetChanged()
    }
}