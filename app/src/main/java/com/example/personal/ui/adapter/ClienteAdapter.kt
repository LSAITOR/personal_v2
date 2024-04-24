package com.example.personal.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personal.data.model.Cliente
import com.example.personal.databinding.ItemsClienteBinding

// 6. Nuestro adaptador debe de heredar de nuestro ClienteViewHolder
class ClienteAdapter(
    // 3. Obligar al quien lo usa a implementar sus miembros (clickDetalle, clickEliminar, clickEditar) solo sera si existen interface
    private val iOnClickListener: IOnClickListener
): RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    // 1. Preparar nuestra lista (interna) de cliente que se almacenara en la memoria del dispositivo (celular, tableta)
    private var lista= emptyList<Cliente>()

    // 2. crear las interfaces para comunicarnos con los botones del modelo(vista)
    // Este paso es necesario solo si nuestra vista tiene botones
    interface IOnClickListener{
        // Las interfaces son un contrato, que obliga a quien lo use, implementarlos
        fun clickInfo(cliente: Cliente)
        fun clickEditar(cliente: Cliente)
        fun clickEliminar(cliente: Cliente)
    }

    // 4. Enlazar un cliente con los  textview del modelo, asi como tambien de los botones
    inner class ClienteViewHolder(private val binding: ItemsClienteBinding) : RecyclerView.ViewHolder(binding.root){
        // 5. Enlazar el objeto cliente con el modelo (vista)
        fun enlazar(cliente: Cliente){
            binding.tvNombre.text = cliente.nombre
            binding.tvDireccion.text = cliente.direccion
            binding.tvEmail.text = cliente.email

            binding.ibEditar.setOnClickListener { iOnClickListener.clickEditar(cliente) }
            binding.ibEliminar.setOnClickListener { iOnClickListener.clickEliminar(cliente) }
            binding.ibInfo.setOnClickListener { iOnClickListener.clickInfo(cliente) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        // 8. Enlazar nuestro ViewHolder con nuestro modelo (vista)
        return ClienteViewHolder(
            // Inflar nuestro modelo
            ItemsClienteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        // 7. Retornar el tamaño de nuestra lista
        return lista.size
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        // 9. Obtener el cliente de la lista segun su posicion, ese objeto cliente sera enviado a la funcion enlazar
        holder.enlazar(lista[position])
    }

    // 10. Crear una funcion que reciba la lista que retorna el ClienteDao (funcion listar) y asigar esa lista a nuestra lista interna (creado en la parte superior)
    fun asignarLista(listaCliente: List<Cliente>){
        // Asignar la listaCliente a nuestra lista interna
        this.lista = listaCliente
        // Notificamos los cambios en la lista interna
        notifyDataSetChanged()
    }
}