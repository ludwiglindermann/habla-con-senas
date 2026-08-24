package com.duoc.hablaconsenas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.duoc.hablaconsenas.navigation.HablaConSenasNavGraph
import com.duoc.hablaconsenas.ui.theme.HablaConSenasTheme

// punto de entrada de la app. onCreate() cumple el mismo rol que main() en un
// programa Kotlin tradicional, y aca solo se delega la UI al NavGraph
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HablaConSenasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    HablaConSenasNavGraph()
                }
            }
        }
    }
}
