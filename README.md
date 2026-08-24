# HablaConSeñas

Proyecto de la actividad Sumativa 1 **"Iniciando la creación de una aplicación móvil con Android Studio"** — Semana 2, Desarrollo de Aplicaciones Móviles (DSY2204), Duoc UC.

## Descripción

Aplicación móvil Android (Kotlin + Jetpack Compose + Material Design 3) de accesibilidad para personas con discapacidad sensorial auditiva, pensada para facilitar su comunicación (escribir y hablar) en su entorno cotidiano.

## Características implementadas

- **Login**: valida el correo y la contraseña contra un arreglo de usuarios registrados (`UsuariosData`), no solo que los campos no estén vacíos.
- **Registro de usuario**: inputs, un combo box (nivel de pérdida auditiva), radio buttons (modo de comunicación preferido) y una checklist de selección múltiple (funciones de accesibilidad). Al registrarse, el usuario se agrega al arreglo y queda disponible de inmediato para iniciar sesión.
- **Recuperar contraseña**: solicitud de recuperación por correo electrónico.
- **Inicio**: destino de inicio fijo tras el login, con acceso a las funciones de comunicación.
- **Escribir**: convierte texto escrito en voz usando el motor TextToSpeech nativo de Android, con control de velocidad de la voz.

## Estructura del proyecto

```
app/src/main/java/com/duoc/hablaconsenas/
├── MainActivity.kt
├── model/Usuario.kt
├── data/UsuariosData.kt          # arreglo con los 5 usuarios de ejemplo
├── navigation/
│   ├── Rutas.kt
│   └── NavGraph.kt
└── ui/
    ├── components/CampoTexto.kt
    ├── screens/
    │   ├── LoginScreen.kt
    │   ├── RegistroScreen.kt
    │   ├── RecuperarPasswordScreen.kt
    │   ├── InicioScreen.kt
    │   └── EscribirScreen.kt
    └── theme/ (Color.kt, Type.kt, Theme.kt)
```

## Usuarios de ejemplo (arreglo inicial)

| Correo | Contraseña |
|---|---|
| javiera.munoz@gmail.com | Javi2024 |
| benjamin.rojas@gmail.com | Rojas123 |
| camila.torres@gmail.com | Camila99 |
| matias.soto@gmail.com | Soto2024 |
| valentina.perez@gmail.com | Valen456 |

## Cómo abrir y ejecutar el proyecto

1. Abre **Android Studio**.
2. `File > Open...` y selecciona la carpeta raíz de este proyecto (idealmente en una ruta sin tildes/ñ/espacios, ej. `C:\AndroidStudioProjects\HablaConSenas`).
3. Espera a que Android Studio sincronice Gradle (descargará el `gradle-wrapper.jar` faltante automáticamente).
4. Ejecuta la app en un emulador o dispositivo físico con **Android 7.0 (API 24)** o superior.

## Requisitos técnicos usados

- Kotlin 1.9.24
- Android Gradle Plugin 8.5.2 / Gradle 8.7
- Jetpack Compose (BOM 2024.06.00) + Material 3
- Navigation Compose 2.7.7
- android.speech.tts.TextToSpeech (SDK de Android)
- compileSdk / targetSdk 34, minSdk 24

## Autor

Ludwig Lindermann — Ingeniería en Desarrollo de Software, Duoc UC.
