package com.duoc.hablaconsenas.data

import androidx.compose.runtime.mutableStateListOf
import com.duoc.hablaconsenas.model.Usuario

object UsuariosData {

    // arreglo con los 5 usuarios de ejemplo que pide la actividad, definidos desde el registro
    val usuariosBase: Array<Usuario> = arrayOf(
        Usuario("Javiera Muñoz", "javiera.munoz@gmail.com", "Javi2024", "Moderada", "Lengua de señas"),
        Usuario("Benjamín Rojas", "benjamin.rojas@gmail.com", "Rojas123", "Severa", "Texto escrito"),
        Usuario("Camila Torres", "camila.torres@gmail.com", "Camila99", "Leve", "Lectura labial"),
        Usuario("Matías Soto", "matias.soto@gmail.com", "Soto2024", "Profunda", "Lengua de señas"),
        Usuario("Valentina Pérez", "valentina.perez@gmail.com", "Valen456", "Moderada", "Texto escrito")
    )

    // lista mutable que parte con los usuarios base y permite sumar nuevos registros durante la sesion
    val usuarios = mutableStateListOf(*usuariosBase)

    fun registrar(usuario: Usuario) {
        usuarios.add(usuario)
    }

    fun existeCorreo(email: String): Boolean {
        return usuarios.any { it.email.equals(email, ignoreCase = true) }
    }

    fun validar(email: String, password: String): Boolean {
        return usuarios.any { it.email.equals(email, ignoreCase = true) && it.password == password }
    }
}
