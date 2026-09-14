package com.duoc.hablaconsenas.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duoc.hablaconsenas.R
import com.duoc.hablaconsenas.ui.components.CampoTexto
import com.duoc.hablaconsenas.util.esCorreoValido

// pantalla de recuperar contraseña
@Composable
fun RecuperarPasswordScreen(
    onVolverALogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var solicitudEnviada by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.recuperar_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.recuperar_descripcion),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!solicitudEnviada) {
                CampoTexto(
                    valor = email,
                    onValorChange = {
                        email = it
                        mostrarError = false
                    },
                    etiqueta = stringResource(R.string.login_email),
                    tipoTeclado = KeyboardType.Email,
                    esError = mostrarError,
                    mensajeError = if (mostrarError) stringResource(R.string.recuperar_error_formato_correo) else null
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // esCorreoValido() es la funcion de extension de util/Validaciones.kt
                        if (email.esCorreoValido()) {
                            solicitudEnviada = true
                        } else {
                            mostrarError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = stringResource(R.string.recuperar_boton))
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.MarkEmailRead,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.recuperar_confirmacion),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onVolverALogin) {
                Text(text = stringResource(R.string.recuperar_volver_login))
            }
        }
    }
}
