package com.example.personal

import android.app.Application
import android.content.Context

class PersonalApp : Application() {

    // Engloba instancias, propiedades, varibales, etc de nivel static
    companion object {
        private var instancia: PersonalApp? = null

        // Crear una funcion que retorne el valor de instancia (context)
        fun getAppContext(): Context {
            return instancia!!.applicationContext
        }
    }

    // Crear el constructor (init)
    init {
        // Damos vamor a instancia
        instancia = this
    }
}