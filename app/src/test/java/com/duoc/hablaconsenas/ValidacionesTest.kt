package com.duoc.hablaconsenas

import com.duoc.hablaconsenas.util.LARGO_MAXIMO_FRASE
import com.duoc.hablaconsenas.util.ejecutarSeguro
import com.duoc.hablaconsenas.util.esCorreoValido
import com.duoc.hablaconsenas.util.esFraseValida
import com.duoc.hablaconsenas.util.esPasswordSegura
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// pruebas unitarias con JUnit de las funciones de util/Validaciones.kt.
// Corren en el computador (carpeta src/test), no necesitan emulador
class ValidacionesTest {

    // --- esCorreoValido ---

    @Test
    fun correoConFormatoCorrecto_esValido() {
        assertTrue("javiera.munoz@gmail.com".esCorreoValido())
        assertTrue("ludwig_l@duocuc.cl".esCorreoValido())
    }

    @Test
    fun correoSinArroba_noEsValido() {
        assertFalse("javiera.munozgmail.com".esCorreoValido())
    }

    @Test
    fun correoSinDominio_noEsValido() {
        assertFalse("javiera@".esCorreoValido())
        assertFalse("javiera@gmail".esCorreoValido())
    }

    @Test
    fun correoVacioOConEspacios_noEsValido() {
        assertFalse("".esCorreoValido())
        assertFalse("javiera munoz@gmail.com".esCorreoValido())
    }

    // --- esPasswordSegura (Firebase pide minimo 6 caracteres) ---

    @Test
    fun passwordDeMenosDeSeisCaracteres_noEsSegura() {
        assertFalse("abc12".esPasswordSegura())
    }

    @Test
    fun passwordDeSeisOMasCaracteres_esSegura() {
        assertTrue("abc123".esPasswordSegura())
        assertTrue("Javi2024".esPasswordSegura())
    }

    // --- esFraseValida ---

    @Test
    fun fraseNormal_esValida() {
        assertTrue("Necesito ayuda".esFraseValida())
    }

    @Test
    fun fraseVaciaOSoloEspacios_noEsValida() {
        assertFalse("".esFraseValida())
        assertFalse("     ".esFraseValida())
    }

    @Test
    fun fraseEnElLimite_esValida_yUnCaracterMas_no() {
        val enElLimite = "a".repeat(LARGO_MAXIMO_FRASE)
        val pasada = "a".repeat(LARGO_MAXIMO_FRASE + 1)
        assertTrue(enElLimite.esFraseValida())
        assertFalse(pasada.esFraseValida())
    }

    // --- ejecutarSeguro (funcion de orden superior con try/catch) ---

    @Test
    fun ejecutarSeguro_sinErrores_devuelveTrueYEjecutaLaAccion() {
        var contador = 0
        val resultado = ejecutarSeguro { contador++ }
        assertTrue(resultado)
        assertEquals(1, contador)
    }

    @Test
    fun ejecutarSeguro_conExcepcion_devuelveFalseSinCaerse() {
        val resultado = ejecutarSeguro { throw IllegalStateException("motor de voz no disponible") }
        assertFalse(resultado)
    }
}
