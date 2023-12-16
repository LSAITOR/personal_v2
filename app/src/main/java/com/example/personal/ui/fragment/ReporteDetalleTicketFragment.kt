package com.example.personal.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal.R
import com.example.personal.data.dao.TicketDao
import com.example.personal.databinding.FragmentReporteDetalleTicketBinding
import com.example.personal.ui.adapter.ReporteDetalleTicketAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReporteDetalleTicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReporteDetalleTicketFragment : Fragment() {

    // vincular el xml con el codigo
    private lateinit var binding: FragmentReporteDetalleTicketBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReporteDetalleTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())
        // Agregar un decorador lineal
        binding.rvLista.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Enlazar el  adaptador con el rvlista
        binding.rvLista.adapter = ReporteDetalleTicketAdapter()

        // Recuperar los datos del ticket que llegara a través de un bundle
        binding.tvCliente.text = arguments?.getString("cliente", "****")
        binding.tvTicket.text = arguments?.getString("ticket", "")
        binding.tvImporte.text = arguments?.getString("total", "0.00")

        // LLamar a la funcion listar
        leerDetalle(arguments?.getInt("ID")?.toInt() ?: 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReporteDetalleTicketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReporteDetalleTicketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun leerDetalle(id: Int) {
        var msgError = ""

        // lanzar la corrutina
        lifecycleScope.launch {
            // indicar el ambito de la corrutina (Dispatchers.IO se utiliza para acceder a base de datos
            // archivos locales o externos, api rest)
            // Main... hace referencia al hilo principal (de preferencia no usar)
            val result = withContext(Dispatchers.IO) {
                // proteger de cualquier error
                try {
                    // Accede a los datos
                    TicketDao.listarDetalleTicket(id)
                } catch (e: Exception) {
                    msgError = e.message.toString()
                    null
                }
            }

            // Salio del hilo secundario
            // Si el msgError no es vacio
            if (msgError.isNotEmpty())
                Toast.makeText(
                    requireContext(), msgError, Toast.LENGTH_LONG
                ).show()

            // Mostrar el resultado en el rvLista
            (binding.rvLista.adapter as ReporteDetalleTicketAdapter).setData(result!!)
        }
    }
}