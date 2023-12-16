package com.example.personal.ui.activity

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
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

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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