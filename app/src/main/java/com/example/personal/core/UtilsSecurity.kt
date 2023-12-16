package com.example.personal.core

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object UtilsSecurity {

    // Clave secreta
    private val valor_clave="Z&K7xD#T@WC=KLQK".toByteArray()

    // Funcion que recibe un parametro, que posteriormente encriptara y sera el resultado devuelto
    fun cifrarDato(datoSinEncriptar: String): String{
        try {
            // Crea una variable que utilizara el algoritmo AES
            val cipher = Cipher.getInstance("AES")

            // Al contructor de ciper pasaremos el modo de encriptacion(ENCRYPT_MODE) y el algorimo a utilizar
            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(valor_clave,"AES")
            )

            // Cipher encripta el dato recibido y sera almacenado en la varibale datoEncriptado
            val datoEncriptado = cipher.doFinal(datoSinEncriptar.toByteArray(charset("UTF-8")))

            // Retornar el dato encriptado en string, especialmente en base64
            return Base64.encodeToString(datoEncriptado, Base64.DEFAULT)
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    // Funcion que recibe un parametro, que posteriormente encriptara y sera el resultado devuelto
    fun descifrarDato(datoEncriptado: String): String{
        try {
            // Crea una variable que utilizara el algoritmo AES
            val cipher = Cipher.getInstance("AES")

            // Al contructor de ciper pasaremos el modo de encriptacion(DECRYPT_MODE) y el algorimo a utilizar
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(valor_clave,"AES")
            )

            // Cipher encripta el dato recibido y sera almacenado en la varibale datoEncriptado
            val datoDesencriptado = cipher.doFinal(
                Base64.decode(datoEncriptado.toByteArray(charset("UTF-8")), Base64.DEFAULT)
            )

            // Retornar el dato desencriptado en string
            return String(datoDesencriptado, charset("UTF-8"))
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }
}