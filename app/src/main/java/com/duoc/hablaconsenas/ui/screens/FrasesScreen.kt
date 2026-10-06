package com.duoc.hablaconsenas.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.hablaconsenas.R
import com.duoc.hablaconsenas.data.FrasesData
import com.duoc.hablaconsenas.model.Frase
import com.duoc.hablaconsenas.ui.components.rememberTextToSpeech
import com.duoc.hablaconsenas.util.ejecutarSeguro
import com.duoc.hablaconsenas.util.esFraseValida
import kotlinx.coroutines.launch

// pantalla "Mis frases": lista las frases guardadas del usuario y permite
// agregarlas, editarlas, eliminarlas y reproducirlas en voz con un toque.
// Es el CRUD de la app, todo guardado en Firebase Realtime Database.
// Editar y eliminar se hacen dentro de la misma tarjeta (sin ventanas
// emergentes), para que sea mas facil de usar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrasesScreen(
    onVolver: () -> Unit
) {
    var frases by remember { mutableStateOf(listOf<Frase>()) }
    var cargando by remember { mutableStateOf(true) }
    var nuevaFrase by remember { mutableStateOf("") }

    // id de la frase que se esta editando o que se quiere eliminar (null = ninguna)
    var idEditando by remember { mutableStateOf<String?>(null) }
    var textoEditado by remember { mutableStateOf("") }
    var idEliminando by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorInvalida = stringResource(R.string.frases_error_invalida)
    val errorConexion = stringResource(R.string.frases_error_conexion)
    val errorReproduccion = stringResource(R.string.escribir_error_reproduccion)

    val textToSpeech = rememberTextToSpeech()

    // vuelve a leer las frases desde Firebase despues de cada cambio
    val recargar: () -> Unit = {
        cargando = true
        FrasesData.obtenerTodas { lista ->
            frases = lista
            cargando = false
        }
    }

    // despues de crear, modificar o eliminar: si salio bien recarga la lista,
    // si fallo avisa con un snackbar
    val alTerminarCambio: (Boolean) -> Unit = { exito ->
        if (exito) {
            recargar()
        } else {
            scope.launch { snackbarHostState.showSnackbar(errorConexion) }
        }
    }

    LaunchedEffect(Unit) { recargar() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.frases_titulo)) },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // agregar una frase nueva (crear)
            OutlinedTextField(
                value = nuevaFrase,
                onValueChange = { nuevaFrase = it },
                label = { Text(stringResource(R.string.frases_campo_nueva)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (!nuevaFrase.esFraseValida()) {
                        scope.launch { snackbarHostState.showSnackbar(errorInvalida) }
                    } else {
                        FrasesData.agregar(nuevaFrase) { exito ->
                            if (exito) nuevaFrase = ""
                            alTerminarCambio(exito)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.frases_boton_agregar))
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                cargando -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                frases.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.frases_vacio),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    )
                }
                else -> {
                    // lista de frases (consultar)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(frases, key = { it.id }) { frase ->
                            when {
                                // la tarjeta se transforma en un campo de texto para modificarla
                                frase.id == idEditando -> TarjetaEditando(
                                    texto = textoEditado,
                                    onTextoChange = { textoEditado = it },
                                    onGuardar = {
                                        if (!textoEditado.esFraseValida()) {
                                            scope.launch { snackbarHostState.showSnackbar(errorInvalida) }
                                        } else {
                                            FrasesData.actualizar(frase.id, textoEditado) { exito -> alTerminarCambio(exito) }
                                            idEditando = null
                                        }
                                    },
                                    onCancelar = { idEditando = null }
                                )
                                // la tarjeta pide confirmacion antes de borrar
                                frase.id == idEliminando -> TarjetaEliminando(
                                    frase = frase,
                                    onConfirmar = {
                                        FrasesData.eliminar(frase.id) { exito -> alTerminarCambio(exito) }
                                        idEliminando = null
                                    },
                                    onCancelar = { idEliminando = null }
                                )
                                else -> TarjetaFrase(
                                    frase = frase,
                                    onReproducir = {
                                        val reproducido = ejecutarSeguro {
                                            textToSpeech?.speak(frase.texto, TextToSpeech.QUEUE_FLUSH, null, frase.id)
                                        }
                                        if (!reproducido) {
                                            scope.launch { snackbarHostState.showSnackbar(errorReproduccion) }
                                        }
                                    },
                                    onEditar = {
                                        idEliminando = null
                                        textoEditado = frase.texto
                                        idEditando = frase.id
                                    },
                                    onEliminar = {
                                        idEditando = null
                                        idEliminando = frase.id
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// tarjeta normal: tocar el texto o el parlante la reproduce en voz
@Composable
private fun TarjetaFrase(
    frase: Frase,
    onReproducir: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onReproducir) {
                Icon(
                    imageVector = Icons.Filled.VolumeUp,
                    contentDescription = stringResource(R.string.frases_reproducir),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = frase.texto,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onReproducir)
                    .padding(vertical = 8.dp)
            )
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.frases_editar_titulo))
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.frases_eliminar))
            }
        }
    }
}

// tarjeta en modo edicion (modificar)
@Composable
private fun TarjetaEditando(
    texto: String,
    onTextoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.frases_editar_titulo),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = texto,
                onValueChange = onTextoChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onGuardar, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.frases_guardar))
                }
                OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.frases_cancelar))
                }
            }
        }
    }
}

// tarjeta que confirma antes de eliminar
@Composable
private fun TarjetaEliminando(
    frase: Frase,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.frases_eliminar_titulo),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "\"${frase.texto}\"",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onConfirmar,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.frases_eliminar))
                }
                OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.frases_cancelar))
                }
            }
        }
    }
}
