package com.duoc.hablaconsenas.model

// frase que el usuario guarda para reproducirla rapido (ej: "Necesito ayuda").
// id es la clave que genera Firebase al guardarla
data class Frase(
    val id: String,
    val texto: String
)
