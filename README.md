# HablaConSeñas

App móvil para la actividad "Iniciando la creación de una aplicación móvil con Android Studio" (Semana 2, DSY2204, Duoc UC).

Es una app de accesibilidad pensada para personas con discapacidad auditiva, para ayudarles a comunicarse (escribir y hablar) en su día a día.

## Qué tiene

- Login validado contra un arreglo de usuarios (no solo revisa que los campos no estén vacíos).
- Registro con combo box, radio buttons y checklist, usando componentes de Material Design. Al registrarse el usuario queda disponible al tiro para iniciar sesión.
- Recuperar contraseña (simulado, no envía correo real).
- Pantalla de inicio con acceso a las funciones principales.
- Escribir: convierte texto en voz usando TextToSpeech de Android, con control de velocidad.
- Escuchar y transcribir: pendiente, se deja como "próximamente".

## Usuarios de prueba

javiera.munoz@gmail.com / Javi2024
benjamin.rojas@gmail.com / Rojas123
camila.torres@gmail.com / Camila99
matias.soto@gmail.com / Soto2024
valentina.perez@gmail.com / Valen456

## Cómo correrlo

Abrir la carpeta del proyecto en Android Studio (en una ruta sin tildes ni ñ), esperar que sincronice Gradle y ejecutar en un emulador o celular con Android 7.0 o superior.

Kotlin + Jetpack Compose + Material 3, minSdk 24, compileSdk 34.

Ludwig Lindermann, Ingeniería en Desarrollo de Software, Duoc UC.
