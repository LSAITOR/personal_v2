package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.data.model.Producto
import com.example.personal.databinding.ItemsCatalogoBinding

class CatalogoAdapter(
    // Un parametro que se envia por constructor
    private val iOnItemCLickListener: CatalogoAdapter.IOnItemCLickListener
): RecyclerView.Adapter<CatalogoAdapter.BindViewHolder>() {

    private var lista: List<Producto> = emptyList()

    // Crear una interfaz (comunicar datos) (Se creara segun sea el caso)
    interface IOnItemCLickListener {
        // Sirve para interactuar con el boton de editar y eliminar de la lista
        // Las interfaces se van a implementar en las clases de destino o donde se usaran (si o si se deben de implementar en la calse de destino)
        fun clickAgregarItem(entidad: Producto)
    }

    // Siempre lo vamos a crear
    inner class BindViewHolder(vista: View): RecyclerView.ViewHolder(vista) {
        // Variable
        val binding = ItemsCatalogoBinding.bind(vista)

        // Funcion para enlazar la vista con los datos (base de datos - tablas)
        fun enlazar(entidad: Producto) {

            binding.tvTitulo.text = entidad.descripcion
            binding.tvCodigoBarra.text = entidad.codigoBarra
            binding.tvPrecio.text = UtilsCommon.formatearDoubleString(entidad.precio)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder {
        // Asociar la vista
        return BindViewHolder(
            // Damos la vista
            LayoutInflater.from(parent.context).inflate(R.layout.items_catalogo, parent, false  )
        )
    }

    override fun onBindViewHolder(holder: BindViewHolder, position: Int) {
        // Obtener los datos
        val item = lista[position]

        // Pasar cada item al binding (vista - diseño)
        holder.enlazar(item)

        // Asociar las acciones de los botones
        holder.binding.ibAgregar.setOnClickListener { iOnItemCLickListener.clickAgregarItem(item) }
    }

    override fun getItemCount(): Int {
        // Debe de retornar el numro total de la lista
        return lista.size
    }

    // Siempre lo crearemos0
    fun setData(_lista: List<Producto>) {
        // Lenamos la lista interna con lo que viene de la consulta sql (_lista)
        this.lista = _lista
        // notificar los cambios
        notifyDataSetChanged()
    }
}