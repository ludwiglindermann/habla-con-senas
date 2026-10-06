package com.duoc.hablaconsenas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.hablaconsenas.data.SesionData
import com.duoc.hablaconsenas.data.UsuariosData
import com.duoc.hablaconsenas.ui.screens.EscribirScreen
import com.duoc.hablaconsenas.ui.screens.FrasesScreen
import com.duoc.hablaconsenas.ui.screens.InicioScreen
import com.duoc.hablaconsenas.ui.screens.LoginScreen
import com.duoc.hablaconsenas.ui.screens.RecuperarPasswordScreen
import com.duoc.hablaconsenas.ui.screens.RegistroScreen

// conecta Login, Registro, Recuperar, Inicio, Escribir y Mis frases
@Composable
fun HablaConSenasNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val contexto = LocalContext.current

    // si el usuario ya tenia la sesion abierta (en Firebase y en SharedPreferences)
    // la app parte directo en Inicio, sin pedirle el login otra vez
    val destinoInicial = remember {
        if (UsuariosData.haySesionActiva() && SesionData.haySesion(contexto)) Rutas.INICIO else Rutas.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = destinoInicial
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(Rutas.REGISTRO) },
                onIrARecuperarPassword = { navController.navigate(Rutas.RECUPERAR) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onRegistroExitoso = { navController.popBackStack(Rutas.LOGIN, inclusive = false) },
                onVolverALogin = { navController.popBackStack() }
            )
        }

        composable(Rutas.RECUPERAR) {
            RecuperarPasswordScreen(
                onVolverALogin = { navController.popBackStack() }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                onIrAEscribir = { navController.navigate(Rutas.ESCRIBIR) },
                onIrAFrases = { navController.navigate(Rutas.FRASES) },
                onCerrarSesion = {
                    // se cierra la sesion en Firebase y se borran los datos guardados
                    UsuariosData.cerrarSesion()
                    SesionData.cerrar(contexto)
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.ESCRIBIR) {
            EscribirScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.FRASES) {
            FrasesScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
