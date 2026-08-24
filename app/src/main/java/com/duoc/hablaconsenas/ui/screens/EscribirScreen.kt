package com.duoc.hablaconsenas.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.duoc.hablaconsenas.R
import kotlinx.coroutines.launch
import java.util.Locale

// pantalla que convierte el texto escrito en voz, usando el motor TextToSpeech nativo de Android
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscribirScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    var mensaje by remember { mutableStateOf("") }
    var velocidad by remember { mutableFloatStateOf(1f) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorVacio = stringResource(R.string.escribir_error_vacio)

    // se crea el motor de texto a voz cuando entramos a la pantalla y se libera al salir
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(Unit) {
        lateinit var instancia: TextToSpeech
        instancia = TextToSpeech(context) { estado ->
            if (estado == TextToSpeech.SUCCESS) {
                instancia.language = Locale("es", "CL")
            }
        }
        textToSpeech = instancia

        onDispose {
            instancia.stop()
            instancia.shutdown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.escribir_titulo)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.escribir_volver))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text(stringResource(R.string.escribir_campo)) },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.escribir_velocidad),
                style = MaterialTheme.typography.titleLarge
            )
            Slider(
                value = velocidad,
                onValueChange = { velocidad = it },
                valueRange = 0.5f..2f
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        if (mensaje.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar(errorVacio) }
                        } else {
                            textToSpeech?.setSpeechRate(velocidad)
                            textToSpeech?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, "mensajeHablaConSenas")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.escribir_boton_reproducir))
                }

                OutlinedButton(
                    onClick = { textToSpeech?.stop() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Icon(Icons.Filled.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.escribir_boton_detener))
                }
            }
        }
    }
}
