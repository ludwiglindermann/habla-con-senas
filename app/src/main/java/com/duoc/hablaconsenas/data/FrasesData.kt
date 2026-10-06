package com.duoc.hablaconsenas.data

import com.duoc.hablaconsenas.model.Frase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// CRUD de las frases guardadas en Firebase Realtime Database.
// Cada usuario tiene sus frases en usuarios/{uid}/frases, asi no se mezclan
object FrasesData {

    private fun referenciaFrases() = FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
        FirebaseDatabase.getInstance().reference.child("usuarios").child(uid).child("frases")
    }

    // crear: push() genera una clave unica para la nueva frase
    fun agregar(texto: String, alTerminar: (exito: Boolean) -> Unit) {
        val referencia = referenciaFrases()
        if (referencia == null) {
            alTerminar(false)
            return
        }
        referencia.push().child("texto").setValue(texto.trim())
            .addOnCompleteListener { alTerminar(it.isSuccessful) }
    }

    // consultar: lee todas las frases del usuario
    fun obtenerTodas(alTerminar: (List<Frase>) -> Unit) {
        val referencia = referenciaFrases()
        if (referencia == null) {
            alTerminar(emptyList())
            return
        }
        referencia.get()
            .addOnSuccessListener { snapshot ->
                val frases = snapshot.children.mapNotNull { hijo ->
                    val id = hijo.key
                    val texto = hijo.child("texto").getValue(String::class.java)
                    if (id != null && texto != null) Frase(id, texto) else null
                }
                alTerminar(frases)
            }
            .addOnFailureListener { alTerminar(emptyList()) }
    }

    // modificar: reemplaza el texto de una frase existente
    fun actualizar(id: String, texto: String, alTerminar: (exito: Boolean) -> Unit) {
        val referencia = referenciaFrases()
        if (referencia == null) {
            alTerminar(false)
            return
        }
        referencia.child(id).child("texto").setValue(texto.trim())
            .addOnCompleteListener { alTerminar(it.isSuccessful) }
    }

    // eliminar: borra la frase completa
    fun eliminar(id: String, alTerminar: (exito: Boolean) -> Unit) {
        val referencia = referenciaFrases()
        if (referencia == null) {
            alTerminar(false)
            return
        }
        referencia.child(id).removeValue()
            .addOnCompleteListener { alTerminar(it.isSuccessful) }
    }
}
