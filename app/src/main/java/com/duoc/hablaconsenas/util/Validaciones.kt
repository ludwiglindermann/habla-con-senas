package com.duoc.hablaconsenas.util

// largo maximo de una frase guardada (lo mismo se valida en las reglas de Firebase)
const val LARGO_MAXIMO_FRASE = 120

// funcion de extension: agrega un comportamiento nuevo a String sin heredar de el
// ni modificar la clase. Se usa igual que un metodo: "correo.esCorreoValido()"
fun String.esCorreoValido(): Boolean {
    val patron = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return patron.matches(this)
}

// Firebase Auth rechaza contraseñas de menos de 6 caracteres, asi que se
// revisa antes para mostrarle al usuario un mensaje claro
fun String.esPasswordSegura(): Boolean = length >= 6

// una frase no puede estar vacia ni pasarse del largo maximo
fun String.esFraseValida(): Boolean = isNotBlank() && trim().length <= LARGO_MAXIMO_FRASE

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
