package com.duoc.hablaconsenas.data

import com.duoc.hablaconsenas.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// Antes los usuarios estaban en un arreglo en memoria y se perdian al cerrar
// la app. Ahora las cuentas las maneja Firebase Authentication y los datos
// del perfil quedan guardados en Realtime Database bajo el uid de cada usuario
object UsuariosData {

    private val auth = FirebaseAuth.getInstance()
    // by lazy: la base de datos se conecta recien la primera vez que se usa
    private val referenciaUsuarios by lazy { FirebaseDatabase.getInstance().reference.child("usuarios") }

    // crea la cuenta y guarda el perfil (nombre, nivel auditivo y modo de comunicacion).
    // La contraseña no se guarda en la base de datos, de eso se encarga Firebase Auth
    fun registrar(usuario: Usuario, alTerminar: (exito: Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(usuario.email, usuario.password)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid
                if (uid == null) {
                    alTerminar(false)
                    return@addOnSuccessListener
                }
                val perfil = mapOf(
                    "nombre" to usuario.nombre,
                    "nivelAuditivo" to usuario.nivelAuditivo,
                    "modoComunicacion" to usuario.modoComunicacion
                )
                referenciaUsuarios.child(uid).child("perfil").setValue(perfil)
                    .addOnCompleteListener { tarea ->
                        // Firebase deja la sesion abierta al crear la cuenta; se cierra
                        // para que el usuario entre desde el login como cualquier otro
                        auth.signOut()
                        alTerminar(tarea.isSuccessful)
                    }
            }
            .addOnFailureListener { alTerminar(false) }
    }

    // valida correo y contraseña con Firebase Auth y busca el nombre del perfil
    fun iniciarSesion(email: String, password: String, alTerminar: (exito: Boolean, nombre: String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { resultado ->
                val uid = resultado.user?.uid
                if (uid == null) {
                    alTerminar(true, "")
                    return@addOnSuccessListener
                }
                referenciaUsuarios.child(uid).child("perfil").child("nombre").get()
                    .addOnSuccessListener { snapshot ->
                        alTerminar(true, snapshot.getValue(String::class.java).orEmpty())
                    }
                    .addOnFailureListener { alTerminar(true, "") }
            }
            .addOnFailureListener { alTerminar(false, "") }
    }

    // envia el correo real de recuperacion de Firebase Auth
    fun enviarCorreoRecuperacion(email: String, alTerminar: (exito: Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { alTerminar(true) }
            .addOnFailureListener { alTerminar(false) }
    }

    fun haySesionActiva(): Boolean = auth.currentUser != null

    fun cerrarSesion() {
        auth.signOut()
    }
}
