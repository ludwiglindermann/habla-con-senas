package com.duoc.hablaconsenas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.hablaconsenas.R
import com.duoc.hablaconsenas.data.UsuariosData
import com.duoc.hablaconsenas.ui.components.CampoTexto
import com.duoc.hablaconsenas.util.esCorreoValido
import kotlinx.coroutines.launch

// pantalla de inicio de sesion, valida contra el arreglo de usuarios registrados
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit,
    onIrARecuperarPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val errorCredenciales = stringResource(R.string.login_error_credenciales)
    val errorFormatoCorreo = stringResource(R.string.login_error_formato_correo)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_subtitulo),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            CampoTexto(
                valor = email,
                onValorChange = {
                    email = it
                    mostrarError = false
                },
                etiqueta = stringResource(R.string.login_email),
                tipoTeclado = KeyboardType.Email,
                esError = mostrarError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                valor = password,
                onValorChange = {
                    password = it
                    mostrarError = false
                },
                etiqueta = stringResource(R.string.login_password),
                esPassword = true,
                esError = mostrarError,
                mensajeError = if (mostrarError) errorCredenciales else null
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onIrARecuperarPassword,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.login_olvido_password))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // esCorreoValido() es la funcion de extension de util/Validaciones.kt
                    when {
                        !email.esCorreoValido() -> {
                            mostrarError = true
                            scope.launch { snackbarHostState.showSnackbar(errorFormatoCorreo) }
                        }
                        !UsuariosData.validar(email, password) -> {
                            mostrarError = true
                            scope.launch { snackbarHostState.showSnackbar(errorCredenciales) }
                        }
                        else -> onLoginExitoso()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(R.string.login_boton))
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onIrARegistro) {
                Text(text = stringResource(R.string.login_sin_cuenta))
            }
        }
    }
}
