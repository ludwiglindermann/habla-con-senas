package com.duoc.hablaconsenas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.duoc.hablaconsenas.ui.screens.EscribirScreen
import com.duoc.hablaconsenas.ui.screens.InicioScreen
import com.duoc.hablaconsenas.ui.screens.LoginScreen
import com.duoc.hablaconsenas.ui.screens.RecuperarPasswordScreen
import com.duoc.hablaconsenas.ui.screens.RegistroScreen

// conecta Login, Registro, Recuperar, Inicio (destino de inicio fijo tras login) y Escribir
@Composable
fun HablaConSenasNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
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
                onCerrarSesion = {
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
    }
}
