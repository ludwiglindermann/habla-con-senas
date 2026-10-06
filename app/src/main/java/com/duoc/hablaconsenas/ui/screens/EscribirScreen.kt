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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.duoc.hablaconsenas.R
import com.duoc.hablaconsenas.data.FrasesData
import com.duoc.hablaconsenas.ui.components.rememberTextToSpeech
import com.duoc.hablaconsenas.util.ejecutarSeguro
import com.duoc.hablaconsenas.util.esFraseValida
import kotlinx.coroutines.launch

// pantalla que convierte el texto escrito en voz, usando el motor TextToSpeech nativo de Android.
// Desde aca tambien se puede guardar el mensaje como frase para usarlo despues
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscribirScreen(
    onVolver: () -> Unit
) {
    var mensaje by remember { mutableStateOf("") }
    var velocidad by remember { mutableFloatStateOf(1f) }
    var guardando by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorVacio = stringResource(R.string.escribir_error_vacio)
    val errorReproduccion = stringResource(R.string.escribir_error_reproduccion)
    val errorFrase = stringResource(R.string.frases_error_invalida)
    val fraseGuardada = stringResource(R.string.escribir_frase_guardada)
    val errorGuardar = stringResource(R.string.frases_error_conexion)

    val textToSpeech = rememberTextToSpeech()

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
                .verticalScroll(rememberScrollState())
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
                            // ejecutarSeguro es la funcion de orden superior de
                            // util/Validaciones.kt: corre la reproduccion dentro de un
                            // try/catch, por si el motor de voz falla en el dispositivo
                            val reproducido = ejecutarSeguro {
                                textToSpeech?.setSpeechRate(velocidad)
                                textToSpeech?.speak(mensaje, TextToSpeech.QUEUE_FLUSH, null, "mensajeHablaConSenas")
                            }
                            if (!reproducido) {
                                scope.launch { snackbarHostState.showSnackbar(errorReproduccion) }
                            }
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

            Spacer(modifier = Modifier.height(12.dp))

            // guarda el mensaje en Firebase para encontrarlo despues en "Mis frases"
            FilledTonalButton(
                onClick = {
                    if (!mensaje.esFraseValida()) {
                        scope.launch { snackbarHostState.showSnackbar(errorFrase) }
                    } else {
                        guardando = true
                        FrasesData.agregar(mensaje) { exito ->
                            guardando = false
                            scope.launch {
                                snackbarHostState.showSnackbar(if (exito) fraseGuardada else errorGuardar)
                            }
                        }
                    }
                },
                enabled = !guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(Icons.Filled.BookmarkAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.escribir_boton_guardar))
            }
        }
    }
}
