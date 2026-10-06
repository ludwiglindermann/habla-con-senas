package com.duoc.hablaconsenas.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.duoc.hablaconsenas.R
import com.duoc.hablaconsenas.data.UsuariosData
import com.duoc.hablaconsenas.model.Usuario
import com.duoc.hablaconsenas.ui.components.CampoTexto
import com.duoc.hablaconsenas.util.esCorreoValido
import com.duoc.hablaconsenas.util.esPasswordSegura
import kotlinx.coroutines.launch

private val opcionesNivelAuditivo = listOf("Leve", "Moderada", "Severa", "Profunda")
private val opcionesModoComunicacion = listOf("Lengua de señas", "Lectura labial", "Texto escrito")
private val opcionesFuncionesAccesibilidad = listOf(
    "Subtítulos automáticos",
    "Vibración en notificaciones",
    "Aviso visual de sonidos"
)

// pantalla de registro: crea la cuenta en Firebase Authentication y guarda el perfil en Realtime Database
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onVolverALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }

    var nivelAuditivoExpandido by remember { mutableStateOf(false) }
    var nivelAuditivoSeleccionado by remember { mutableStateOf(opcionesNivelAuditivo.first()) }

    var modoComunicacionSeleccionado by remember { mutableStateOf(opcionesModoComunicacion.first()) }

    val funcionesSeleccionadas = remember { mutableStateOf(setOf<String>()) }

    var aceptaTerminos by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    var cargando by remember { mutableStateOf(false) }

    val contexto = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val errorCampos = stringResource(R.string.registro_error_campos)
    val errorPassword = stringResource(R.string.registro_error_password)
    val errorTerminos = stringResource(R.string.registro_error_terminos)
    val errorExistente = stringResource(R.string.registro_error_existente)
    val errorFormatoCorreo = stringResource(R.string.registro_error_formato_correo)
    val errorPasswordCorta = stringResource(R.string.registro_error_password_corta)
    val mensajeExito = stringResource(R.string.registro_exito)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.registro_titulo),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            CampoTexto(
                valor = nombre,
                onValorChange = { nombre = it },
                etiqueta = stringResource(R.string.registro_nombre)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                valor = email,
                onValorChange = { email = it },
                etiqueta = stringResource(R.string.registro_email),
                tipoTeclado = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                valor = password,
                onValorChange = { password = it },
                etiqueta = stringResource(R.string.registro_password),
                esPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                valor = confirmarPassword,
                onValorChange = { confirmarPassword = it },
                etiqueta = stringResource(R.string.registro_confirmar_password),
                esPassword = true,
                esError = mostrarError && confirmarPassword != password,
                mensajeError = if (mostrarError && confirmarPassword != password) errorPassword else null
            )

            Spacer(modifier = Modifier.height(24.dp))

            // combo box con el nivel de perdida auditiva
            Text(
                text = stringResource(R.string.registro_nivel_auditivo),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = nivelAuditivoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { nivelAuditivoExpandido = true }
                )
                DropdownMenu(
                    expanded = nivelAuditivoExpandido,
                    onDismissRequest = { nivelAuditivoExpandido = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    opcionesNivelAuditivo.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                nivelAuditivoSeleccionado = opcion
                                nivelAuditivoExpandido = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // radio buttons con el modo de comunicacion preferido
            Text(
                text = stringResource(R.string.registro_modo_comunicacion),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                opcionesModoComunicacion.forEach { opcion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (opcion == modoComunicacionSeleccionado),
                                onClick = { modoComunicacionSeleccionado = opcion },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (opcion == modoComunicacionSeleccionado),
                            onClick = { modoComunicacionSeleccionado = opcion }
                        )
                        Text(text = opcion, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // checklist con las funciones de accesibilidad a activar
            Text(
                text = stringResource(R.string.registro_funciones_accesibilidad),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                opcionesFuncionesAccesibilidad.forEach { opcion ->
                    val marcado = funcionesSeleccionadas.value.contains(opcion)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = marcado,
                            onCheckedChange = { marcarlo ->
                                funcionesSeleccionadas.value = if (marcarlo) {
                                    funcionesSeleccionadas.value + opcion
                                } else {
                                    funcionesSeleccionadas.value - opcion
                                }
                            }
                        )
                        Text(text = opcion, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = { aceptaTerminos = it }
                )
                Text(
                    text = stringResource(R.string.registro_terminos),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // esCorreoValido() es la funcion de extension de util/Validaciones.kt
                    when {
                        nombre.isBlank() || email.isBlank() || password.isBlank() || confirmarPassword.isBlank() -> {
                            mostrarError = true
                            scope.launch { snackbarHostState.showSnackbar(errorCampos) }
                        }
                        !email.esCorreoValido() -> {
                            mostrarError = true
                            scope.launch { snackbarHostState.showSnackbar(errorFormatoCorreo) }
                        }
                        !password.esPasswordSegura() -> {
                            mostrarError = true
                            scope.launch { snackbarHostState.showSnackbar(errorPasswordCorta) }
                        }
                        password != confirmarPassword -> {
                            mostrarError = true
                            scope.launch { snackbarHostState.showSnackbar(errorPassword) }
                        }
                        !aceptaTerminos -> {
                            scope.launch { snackbarHostState.showSnackbar(errorTerminos) }
                        }
                        else -> {
                            // ya no se revisa el correo repetido en una lista local: si el
                            // correo ya tiene cuenta, Firebase responde con error
                            cargando = true
                            UsuariosData.registrar(
                                Usuario(
                                    nombre = nombre,
                                    email = email,
                                    password = password,
                                    nivelAuditivo = nivelAuditivoSeleccionado,
                                    modoComunicacion = modoComunicacionSeleccionado
                                )
                            ) { exito ->
                                cargando = false
                                if (exito) {
                                    // Toast en vez de snackbar: esta pantalla se cierra al volver
                                    // al login y el snackbar alcanzaba a desaparecer sin verse
                                    Toast.makeText(contexto, mensajeExito, Toast.LENGTH_LONG).show()
                                    onRegistroExitoso()
                                } else {
                                    scope.launch { snackbarHostState.showSnackbar(errorExistente) }
                                }
                            }
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (cargando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = stringResource(R.string.registro_boton))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onVolverALogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.registro_volver_login))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
