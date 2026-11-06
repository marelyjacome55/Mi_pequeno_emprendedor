package com.example.mi_pequeno_emprendedor.ui.registro

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistroValidatorTest {

    @Test
    fun `datos validos no producen errores`() {
        val errores = validarRegistro(
            nombre = "Ana",
            apellido = "Pérez",
            nickname = "anap",
            fechaNacimiento = "15/03/2000",
            email = "ana@correo.com",
            password = "secreta1",
            confirmacionPassword = "secreta1"
        )
        assertFalse(errores.hayErrores)
    }

    @Test
    fun `campos vacios y correo invalido producen errores`() {
        val errores = validarRegistro(
            nombre = "",
            apellido = "",
            nickname = "",
            fechaNacimiento = "",
            email = "ana-sin-arroba",
            password = "123",
            confirmacionPassword = "123"
        )
        assertTrue(errores.hayErrores)
        assertEquals("Ingresa un correo válido", errores.email)
        assertEquals("Mínimo 6 caracteres", errores.password)
    }

    @Test
    fun `contrasenas distintas producen error`() {
        val errores = validarRegistro(
            nombre = "Ana",
            apellido = "Pérez",
            nickname = "anap",
            fechaNacimiento = "15/03/2000",
            email = "ana@correo.com",
            password = "secreta1",
            confirmacionPassword = "secreta2"
        )
        assertEquals("Las contraseñas no coinciden", errores.confirmacionPassword)
    }

    @Test
    fun `confirmacion vacia o distinta se marca`() {
        assertEquals(
            "Confirma tu contraseña",
            validarRegistro("Ana", "Pérez", "anap", "15/03/2000", "ana@correo.com", "secreta1", "")
                .confirmacionPassword
        )
        assertEquals(
            "Las contraseñas no coinciden",
            validarRegistro("Ana", "Pérez", "anap", "15/03/2000", "ana@correo.com", "123", "124")
                .confirmacionPassword
        )
        assertEquals("Las contraseñas no coinciden", errorConfirmacion("123", "124"))
        assertNull(errorConfirmacion("123", ""))
    }

    @Test
    fun `fecha futura e invalida son rechazadas`() {
        assertNull(fechaAIso("01/01/2999"))
        assertNull(fechaAIso("31/02/2000"))
        assertEquals("2000-03-15", fechaAIso("15/03/2000"))
    }
}
