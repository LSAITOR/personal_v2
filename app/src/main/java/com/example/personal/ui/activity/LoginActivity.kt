package com.example.personal.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.personal.core.PreferenciasKey
import com.example.personal.core.ProveedorPreferencia
import com.example.personal.data.dao.UsuarioDao
import com.example.personal.databinding.ActivityLoginBinding
import com.example.personal.ui.dialog.DialogoConfigServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    //Crear el binding--> Relacion o enlace con la interfaz de usuario
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Damos valor a la proomesa (binding)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hacemos uso del boton acceder
        binding.btAcceder.setOnClickListener {
            // Verificar si se ha configurado los datos del servidor
            if (ProveedorPreferencia.getPreferencia(PreferenciasKey.CONFIGURAR_SERVER)
                    .isNullOrEmpty()
            ) {
                Toast.makeText(this, "El servidor no esta configurado", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validar que las cajas de textos tengan valores
            if (binding.etUsuario.text.toString().isEmpty() ||
                binding.etClave.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "El usuario y clave son obligatorios", Toast.LENGTH_LONG)
                    .show()
                // Abandonar el proceso, NO continua, da por terminado la accion
                return@setOnClickListener
            }

            // Validar el usuario y clave en la base de datos
            acceder(
                binding.etUsuario.text.toString().trim(),
                binding.etClave.text.toString().trim()
            )
        }

        binding.tvConfigServer.setOnClickListener {
            // Mostrar el cuadro de dialogo para configurar el server
            DialogoConfigServer.newInstance().show(supportFragmentManager, "ConfigServer")

            /*Toast.makeText(
                this@LoginActivity,
                ProveedorPreferencia.getPreferencia(PreferenciasKey.CONFIGURAR_SERVER),
                Toast.LENGTH_LONG).show()*/
        }
    }

    private fun acceder(usuario: String, clave: String) {
        // Usar corrutinas para acceder a la base datos
        lifecycleScope.launch {
            // Crear una variable para almcenar cualquier error o excepcion que retorne la base de datos
            var msg = ""

            //Lanzar la corrutina en un hilo secundario de tipo IO, almacenando el resultado en result
            val result = withContext(Dispatchers.IO) {
                // Protege el hilo secunario y captura cualquier error, dicho se almacenara en la variable msg
                try {
                    // Accede al login de UsuarioDao (internamente accede a la db)
                    UsuarioDao.login(usuario, clave)
                } catch (e: Exception) {
                    // Capturar el error
                    msg = e.message.toString()
                    null
                }
            }

            // Verificar si existe error
            if (msg.isNotEmpty()) {
                Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                // Abandonar el proceso
                return@launch
            }

            // Si el result, contiene al objeto usuario validado, entonces acceder a la actividad principal (menu y demas fragmentos)
            if (result != null) {
                // startActivity -> Inicia una actividad
                // Intent -> Intento de lanzar desde un origen a un destino --- Intent(inicio, destino)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                //Finalizar el LoginActivity
                finish()
            }
        }
    }
}