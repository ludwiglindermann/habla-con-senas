package com.duoc.hablaconsenas.model

// representa a un usuario registrado en la app
data class Usuario(
    val nombre: String,
    val email: String,
    val password: String,
    val nivelAuditivo: String,
    val modoComunicacion: String
)
