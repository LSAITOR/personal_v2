package com.example.personal.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.personal.core.PreferenciasKey
import com.example.personal.core.ProveedorPreferencia
import com.example.personal.core.UtilsSecurity
import com.example.personal.databinding.DialogoConfigServerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogoConfigServer: DialogFragment() {

    private lateinit var binding: DialogoConfigServerBinding

    //Creamos  una funcion estatica para poder acceder a la clase DialogoConfigServer sin crear un objeto
    companion object{
        fun newInstance(): DialogoConfigServer{
            // Crea una unica instancia de DialogoConfigServer
            val instancia = DialogoConfigServer()
            // El dialogo no pueda ser cancelado por el usuario
            instancia.isCancelable=false

            // Retornar la instancia de la clase
            return instancia
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //return super.onCreateDialog(savedInstanceState)
        // Damos valor al binding
        binding = DialogoConfigServerBinding.inflate(onGetLayoutInflater(savedInstanceState))

        // Recuperar la cadena de conexion
        val cadena = UtilsSecurity.descifrarDato(
            ProveedorPreferencia.getPreferencia(PreferenciasKey.CONFIGURAR_SERVER) ?: ""
        ).toString().split(";")

        //Mostrar el valor de la cadena
        binding.etIpServer.setText(cadena[0])
        


        // Asignar el diseño
        return MaterialAlertDialogBuilder(requireContext()).apply {
            setCancelable(false)
            setTitle("Config Server")
            setView(binding.root)

            setPositiveButton("Aceptar") {dialog, _ ->
                //Verificar si existe alguna caja de texto vacio
                if(binding.etIpServer.text.toString().trim().isEmpty() ||
                        binding.etPuerto.text.toString().trim().isEmpty() ||
                        binding.etUsuario.text.toString().trim().isEmpty() ||
                        binding.etClave.text.toString().trim().isEmpty()){
                    Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }

                try {
                    // Escribir la cadena de conexion en el archivo de configuracion, pasando los datos de las cajas de texto.
                    ProveedorPreferencia.setConfigurarServer(
                        UtilsSecurity.cifrarDato(
                            "jdbc:jtds:sqlserver://${binding.etIpServer.text.toString().trim()}:${binding.etPuerto.text.toString().trim()};databaseName=CONSURTARBD;user=${binding.etUsuario.text.toString().trim()};password=${binding.etClave.text.toString().trim()};"
                        )
                    )

                    // Si la escritura fue exitosa, entonces mostrar mensaje de exito
                    Toast.makeText(requireContext(), "La configuracion fue exitosa", Toast.LENGTH_LONG).show()
                } catch (e: Exception) { Toast.makeText(requireContext(), "Datos del servidor grabados correctamente", Toast.LENGTH_LONG).show()
                    Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_LONG).show()
                }

                dialog.dismiss()
            }

            setNegativeButton("Cancelar") {dialog, _ ->
                dialog.cancel()
            }
        }.create()
    }

}