# HablaConSeñas

App móvil para las actividades "Iniciando la creación de una aplicación móvil con Android Studio" (Semana 2) e "Integrando Kotlin a la aplicación móvil con Android Studio" (Semana 5), DSY2204, Duoc UC.

Es una app de accesibilidad pensada para personas con discapacidad auditiva, para ayudarles a comunicarse (escribir y hablar) en su día a día.

## Qué tiene

- Login validado contra un arreglo de usuarios (correo y contraseña reales, más el formato del correo con una función de extensión).
- Registro con combo box, radio buttons y checklist, usando componentes de Material Design. Valida formato de correo, correo duplicado y que las contraseñas coincidan; al registrarse el usuario queda disponible al tiro para iniciar sesión.
- Recuperar contraseña (simulado, no envía correo real), también valida el formato del correo.
- Pantalla de inicio con acceso a las funciones principales.
- Escribir: convierte texto en voz usando TextToSpeech de Android, con control de velocidad. Si el idioma configurado no está disponible en el dispositivo, usa el idioma por defecto del sistema como respaldo, y la reproducción corre dentro de una función de orden superior con try/catch por si el motor de voz falla.
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
