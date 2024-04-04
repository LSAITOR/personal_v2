package com.example.personal.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.personal.R
import com.example.personal.data.model.DetalleTicket
import com.example.personal.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    // Variables
    // lateinit --> Promesa
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializando la variable binding _> Asignando un valor inicial
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_empleado, R.id.nav_producto, R.id.nav_reporte_ticket
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.menuCerrarSesion.setOnClickListener {
            // Cierra el menu principal
            drawerLayout.closeDrawers()

            // Mostrar un mensaje, indicando si desea cerrar la sesion
            val alerta = MaterialAlertDialogBuilder(this)
            alerta.setCancelable(false) // Debe intervenir el usuario para cerrar el dialogo
            alerta.setTitle("Cerrar Sesion")
            alerta.setMessage("¿Esta seguro de querer cerrar la sesion?")

            alerta.setPositiveButton("SI"){ dialog, _ ->
                // Mostrar el login
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )

                // Finalizar el MainActivity
                finish()

                dialog.dismiss()
            }

            alerta.setNegativeButton("NO"){ dialog, _ ->
                dialog.cancel()
            }

            // Crear la alerta y mostrarlo
            alerta.create().show()
        }

        binding.menuSalir.setOnClickListener {
            // Cerrar el menu principal
            drawerLayout.closeDrawers()

            // Mostrar un mensaje, indicando si desea cerrar la sesion
            val alerta = MaterialAlertDialogBuilder(this)
            alerta.setCancelable(false) // Debe intervenir el usuario para cerrar el dialogo
            alerta.setTitle("SALIR")
            alerta.setMessage("¿Esta seguro que desea salir de la aplicacion?")

            alerta.setPositiveButton("SI"){ dialog, _ ->
                // Finalizar el MainActivity
                finish()
                //Cerrar el mensaje (dialogo)
                dialog.dismiss()

                //Salir del proceso (hilo) principal
                exitProcess(0)
            }

            alerta.setNegativeButton("NO"){ dialog, _ ->
                dialog.cancel()
            }

            // Crear la alerta y mostrarlo
            alerta.create().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        // Listar mutable de tipo DetalleTicket
        var detalleCarrito = mutableListOf<DetalleTicket>()

        fun limpiarListaCarrito() {
            detalleCarrito.clear()
        }

        fun calcular(): Int {
            var cantidad = 0
            // recorrer la lista de detalleCarrito, si tuviera elementos
            for (det in detalleCarrito)
                cantidad += det.cantidad

            // Rerorna el valor de la variable cantidad
            return cantidad
        }

        fun calcularTotal(): Double {
            // Variable total
            var total = 0.0
            // recorrer la lista de detalleCarrito, si tuviera elementos
            for (det in detalleCarrito)
                total += det.cantidad * det.precio

            // Rerorna el valor de la variable total
            return total
        }

        fun calcularTotalImporte(): Double {
            var total = 0.0
            // recorrer la lista de detalleCarrito, si tuviera elementos
            // Operador acumulador += suma y almacena el nuevo valor (total = total + importe)
            // Restar -=
            // Multiplicar *=

            for (det in detalleCarrito)
                total += det.importe

            // Rerorna el valor de la variable total
            return total
        }
    }
}