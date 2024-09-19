package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal.R
import com.example.personal.data.dao.ClienteDao
import com.example.personal.data.model.Cliente
import com.example.personal.databinding.FragmentClienteBinding
import com.example.personal.ui.adapter.ClienteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// 12. indicar con que interface va a trabajar nuestro ClienteFragment
class ClienteFragment : Fragment(), ClienteAdapter.IOnClickListener {

    // 1. Crear una promesa (lateinit var) para el binding, antes de ser usado debera ser creado
    private lateinit var binding: FragmentClienteBinding

    // Funcion crea la vista
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 2. Cumplir la promesa, asignando un valor al binding
        binding = FragmentClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Despues de que se ha creado la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 12. Configurar nuestro Reciclador (RecyclerView)
        // La forma en que se mostrara los elementos (Lineal)
        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())
        // Indicamos con que adaptador trabajara el reciclador
        binding.rvLista.adapter = ClienteAdapter(this)

        // 13. Visualizar el fragmento de operacion cliente
        binding.fabNuevo.setOnClickListener {
            Navigation.findNavController(it).navigate(
                R.id.action_clienteFragment_to_operacionClienteFragment
            )
        }

        // Llamar a la funcion listarCliente
        leerCliente("")
    }

    // 3. Crear una funcion que reciba un parametro y este llame a la funcion listar de ClienteDao
    // y esta funcion debe ejecutarse en un hilo secundario
    private fun leerCliente(dato: String){
        // 4. Crear una variable para almacenar algun devuelto por el dao
        var msgError=""

        // 5. Lanzar una corrutina (Preparando)
        lifecycleScope.launch {
            //6. indicar al usuario de que se estra trabajando en su operacion
            binding.progressBar.isVisible=true

            //7. Lanzar el hilo secundario indicando su contexto(IO) este hilo se ejecuta en segundo plano
            // Y el resultado sera guardado en una variable
            val result= withContext(Dispatchers.IO){
                // Contexto Dispatchers.IO: Se usa para procesos pesados, calculos, acceso a datos (locales, remotros, restapi)
                //Contexto Dispatchers.MAIN: Se usara exclusivamente para la interfaz grafica (xml)

            // Aqui estara el bloque secundario (hilo)
                // Las acciones deben de estrar protegio por try - catch
                try {
                    // 8. LLamar a la funcion listar del ClienteDao
                    ClienteDao.listar(dato)
                } catch (e: Exception){
                    // Pasar el error captura a la variable msgError
                    msgError = e.message.orEmpty()
                    // En caso de error, devolveremos una lista vacía
                    emptyList<Cliente>()
                }
            }

            //9. Indicar al usuario de que el proceso ya culmino (ocultar el progressbar)
            binding.progressBar.isVisible=false

            //10. Verificar si la variable msgError tiene datos, de ser asi, mostrar el mensaje
            if(msgError.isNotEmpty())
                Toast.makeText(requireContext(), msgError, Toast.LENGTH_LONG).show()

            //11. Asignar el resultado de la corruna al adaptador
            (binding.rvLista.adapter as ClienteAdapter).asignarLista(result)
        }
    }

    override fun clickInfo(cliente: Cliente) {

    }

    override fun clickEditar(cliente: Cliente) {

    }

    override fun clickEliminar(cliente: Cliente) {

    }
}