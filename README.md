# HablaConSeñas

App móvil para las actividades "Iniciando la creación de una aplicación móvil con Android Studio" (Semana 2), "Integrando Kotlin a la aplicación móvil con Android Studio" (Semana 5) y "Preparación de una aplicación móvil Android/Kotlin para su distribución" (Semana 8), DSY2204, Duoc UC.

Es una app de accesibilidad pensada para personas con discapacidad auditiva, para ayudarles a comunicarse (escribir y hablar) en su día a día.

## Qué tiene

- Login con Firebase Authentication. Valida el formato del correo con una función de extensión antes de consultar a Firebase.
- Sesión guardada con SharedPreferences (nombre y correo): si la sesión sigue abierta, la app parte directo en Inicio. Al cerrar sesión se borra.
- Registro con combo box, radio buttons y checklist. La cuenta se crea en Firebase Authentication y el perfil (nombre, nivel auditivo y modo de comunicación) queda en Realtime Database. Valida formato de correo, largo mínimo de la contraseña y que las contraseñas coincidan.
- Recuperar contraseña: envía el correo real de Firebase.
- Inicio con saludo por nombre y acceso a las funciones.
- Escribir: convierte texto en voz con TextToSpeech, con control de velocidad. El mensaje se puede guardar en "Mis frases". Si el idioma no está disponible usa el del sistema, y la reproducción corre dentro de una función de orden superior con try/catch.
- Mis frases (CRUD en Realtime Database): agregar, ver, editar y eliminar las frases que más se usan, y reproducirlas en voz con un toque.
- Escuchar y transcribir: pendiente, se deja como "próximamente".

## Firebase

La app usa un proyecto de Firebase con Authentication (correo/contraseña) y Realtime Database. El archivo `app/google-services.json` es el de ese proyecto. Las reglas de la base de datos están en `firebase-database-rules.json`: cada usuario solo puede leer y escribir sus propios datos, y una frase no puede quedar vacía ni pasar de 120 caracteres.

Ya no hay usuarios de prueba fijos, hay que registrarse desde la app.

## Pruebas

Pruebas unitarias con JUnit en `app/src/test/java/com/duoc/hablaconsenas/ValidacionesTest.kt` (validación de correo, contraseña, frases y la función `ejecutarSeguro`). Se corren en Android Studio con clic derecho sobre el archivo > Run 'ValidacionesTest', no necesitan emulador.

## APK

El APK se genera firmado desde Build > Generate Signed App Bundle / APK > APK, variante release. La llave (.jks) no se sube al repositorio. Versión actual: versionCode 2, versionName 1.1.

## Cómo correrlo

Abrir la carpeta del proyecto en Android Studio (en una ruta sin tildes ni ñ), esperar que sincronice Gradle y ejecutar en un emulador o celular con Android 7.0 o superior y conexión a internet.

Kotlin + Jetpack Compose + Material 3 + Firebase, minSdk 24, compileSdk 34.

Ludwig Lindermann, Ingeniería en Desarrollo de Software, Duoc UC.
