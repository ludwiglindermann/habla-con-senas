package com.duoc.hablaconsenas.ui.components

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

// crea el motor de texto a voz al entrar a una pantalla y lo libera al salir.
// Antes estaba dentro de EscribirScreen; se movio aca para usarlo tambien en
// la pantalla de frases guardadas sin repetir el mismo codigo
@Composable
fun rememberTextToSpeech(): TextToSpeech? {
    val context = LocalContext.current
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        lateinit var instancia: TextToSpeech
        instancia = TextToSpeech(context) { estado ->
            if (estado == TextToSpeech.SUCCESS) {
                // si el idioma configurado no esta disponible en el dispositivo,
                // se usa el idioma por defecto del sistema como respaldo
                val resultadoIdioma = instancia.setLanguage(Locale("es", "CL"))
                if (resultadoIdioma == TextToSpeech.LANG_MISSING_DATA ||
                    resultadoIdioma == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    instancia.setLanguage(Locale.getDefault())
                }
            }
        }
        textToSpeech = instancia

        onDispose {
            instancia.stop()
            instancia.shutdown()
        }
    }

    return textToSpeech
}
