package com.example.personal.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personal.R
import com.example.personal.core.UtilsCommon
import com.example.personal.core.UtilsDate
import com.example.personal.data.dao.TicketDao
import com.example.personal.data.model.Ticket
import com.example.personal.databinding.FragmentReporteTicketBinding
import com.example.personal.ui.adapter.ReporteTicketAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReporteTicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReporteTicketFragment : Fragment(), ReporteTicketAdapter.IOnClickListener {

    private lateinit var binding: FragmentReporteTicketBinding

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
        binding = FragmentReporteTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLista.layoutManager = LinearLayoutManager(requireContext())

        // Enlazar el  adaptador con el rvlista
        binding.rvLista.adapter = ReporteTicketAdapter(this)

        // al hacer clic en la caja de texto se mostrara el calendario
        binding.etDesde.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etDesde, parentFragmentManager)
        }

        binding.etHasta.setOnClickListener {
            UtilsDate.mostrarCalendario(binding.etHasta, parentFragmentManager)
        }

        binding.etDesde.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                // Si la caja de texto desde y hasta NO estan vacio
                if(binding.etDesde.text.toString().isNotEmpty() &&
                    binding.etHasta.text.toString().isNotEmpty()) {
                    listar(binding.etDesde.text.toString().trim(), binding.etHasta.text.toString().trim())
                }
            }

        })

        binding.etHasta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                // Si la caja de texto desde y hasta NO estan vacio
                if(binding.etDesde.text.toString().isNotEmpty() &&
                    binding.etHasta.text.toString().isNotEmpty()) {
                    listar(binding.etDesde.text.toString().trim(), binding.etHasta.text.toString().trim())
                }
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReporteTicketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReporteTicketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun listar(desde: String, hasta: String) {
        var msgError = ""

        // lanzar la corrutina
        lifecycleScope.launch {
            // indicar el ambito de la corrutina (Dispatchers.IO se utiliza para acceder a base de datos
            // archivos locales o externos, api rest)
            // Main... hace referencia al hilo principal (de preferencia no usar)
            val result = withContext(Dispatchers.IO) {
                // proteger de cualquier error
                try {
                    TicketDao.listarTicket(desde, hasta)
                } catch (e: Exception) {
                    msgError=e.message.toString()
                    null
                }
            }

            // Salio del hilo secundario
            if(msgError.isNotEmpty())
                Toast.makeText(
                    requireContext(), msgError, Toast.LENGTH_LONG
                ).show()

            // Mostrar el resultado en el rvLista
            (binding.rvLista.adapter as ReporteTicketAdapter).setData(result!!)
        }
    }

    private fun anular(idTicket: Int, desde: String, hasta: String) {
        var msgError = ""

        // lanzar la corrutina
        lifecycleScope.launch {
            // indicar el ambito de la corrutina (Dispatchers.IO se utiliza para acceder a base de datos
            // archivos locales o externos, api rest)
            // Main... hace referencia al hilo principal (de preferencia no usar)
            val result = withContext(Dispatchers.IO) {
                // proteger de cualquier error
                try {
                    TicketDao.anularTicket(idTicket)
                    TicketDao.listarTicket(desde, hasta)
                } catch (e: Exception) {
                    msgError=e.message.toString()
                    null
                }
            }

            // Salio del hilo secundario
            if(msgError.isNotEmpty())
                Toast.makeText(
                    requireContext(), msgError, Toast.LENGTH_LONG
                ).show()
            else
                Toast.makeText(
                    requireContext(), "Registro anulado correctamente", Toast.LENGTH_LONG
                ).show()

            // Mostrar el resultado en el rvLista
            (binding.rvLista.adapter as ReporteTicketAdapter).setData(result!!)
            //listar(desde, hasta)
        }
    }

    override fun clickAnular(entidad: Ticket) {
        // Verificar si el ticket esta anulado
        if(entidad.estado.uppercase() == "ANULADO") {
            Toast.makeText(
                requireContext(),
                "El ticket ${entidad.id.toString()} ya esta anulado", Toast.LENGTH_LONG
            ).show()

            // return = Abandonar el proceso o secuencia de ejecucion
            return
        }

        MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("ANULAR TICKET")
            setMessage("¿Esta seguro de querer anular el Ticket: ${entidad.id}?")
            setPositiveButton("SI") { dialog, _ ->
                // Anular el ticket
                anular(entidad.id, binding.etDesde.text.toString(), binding.etHasta.text.toString())

                dialog.dismiss()
            }
            setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    override fun clickDetalle(entidad: Ticket) {
        // Navegar al fragmento ReporteDetalleTicket
        Navigation.findNavController(requireView()).navigate(
            R.id.action_nav_reporte_ticket_to_reporteDetalleTicketFragment,
            bundleOf(
                Pair("cliente", entidad.cliente),
                Pair("ticket", "TICKET: ${entidad.id.toString()}"),
                Pair("total", "TOTAL ${UtilsCommon.formatearDoubleString(entidad.total)}"),
                Pair("ID", entidad.id)
            )
        )
    }
}