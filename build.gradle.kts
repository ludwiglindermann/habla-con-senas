// Build file de nivel superior, opciones comunes a todos los modulos
plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    // conecta el proyecto con Firebase leyendo app/google-services.json
    id("com.google.gms.google-services") version "4.4.2" apply false
}
