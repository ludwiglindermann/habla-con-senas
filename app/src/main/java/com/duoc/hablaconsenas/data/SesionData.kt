package com.duoc.hablaconsenas.data

import android.content.Context
import androidx.core.content.edit

// datos basicos de la sesion guardados con SharedPreferences: el nombre y el
// correo del usuario que inicio sesion. Sirve para saludarlo en Inicio y para
// que no tenga que volver a escribir sus datos cada vez que abre la app
object SesionData {

    private const val ARCHIVO = "sesion_habla_con_senas"
    private const val CLAVE_NOMBRE = "nombre"
    private const val CLAVE_CORREO = "correo"

    private fun preferencias(contexto: Context) =
        contexto.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    // edit {} es la extension de Android KTX que reemplaza edit()...apply()
    fun guardar(contexto: Context, nombre: String, correo: String) {
        preferencias(contexto).edit {
            putString(CLAVE_NOMBRE, nombre)
            putString(CLAVE_CORREO, correo)
        }
    }

    fun nombre(contexto: Context): String =
        preferencias(contexto).getString(CLAVE_NOMBRE, "").orEmpty()

    fun haySesion(contexto: Context): Boolean =
        preferencias(contexto).contains(CLAVE_CORREO)

    fun cerrar(contexto: Context) {
        preferencias(contexto).edit { clear() }
    }
}
