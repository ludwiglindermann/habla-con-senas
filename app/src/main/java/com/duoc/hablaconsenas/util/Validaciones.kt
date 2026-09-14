package com.duoc.hablaconsenas.util

// funcion de extension: agrega un comportamiento nuevo a String sin heredar de el
// ni modificar la clase. Se usa igual que un metodo: "correo.esCorreoValido()"
fun String.esCorreoValido(): Boolean {
    val patron = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return patron.matches(this)
}

// funcion de orden superior: recibe otra funcion (accion) como parametro y la
// ejecuta dentro de un try/catch. Devuelve true si no hubo errores y false si
// la accion lanzo una excepcion (por ejemplo, si el motor de voz falla)
fun ejecutarSeguro(accion: () -> Unit): Boolean {
    return try {
        accion()
        true
    } catch (e: Exception) {
        false
    }
}
