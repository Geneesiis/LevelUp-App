package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {
    private val repositorio = UsuarioRepository()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    private val _errorMensaje = MutableStateFlow<String?>(null)
    val errorMensaje: StateFlow<String?> = _errorMensaje

    fun registroUsuario(correo: String, clave: String, confirmarClave: String, nombre: String) {
        // Validaciones locales
        if (correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty() || nombre.isEmpty()) {
            _errorMensaje.value = "Todos los campos son obligatorios"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            _errorMensaje.value = "Ingresa un correo válido"
            return
        }

        if (clave != confirmarClave) {
            _errorMensaje.value = "Las contraseñas no coinciden"
            return
        }

        if (clave.length < 6) {
            _errorMensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        // Proceso de registro
        _cargando.value = true
        _errorMensaje.value = null

        viewModelScope.launch {
            try {
                val exitoso = repositorio.registroUsuario(correo, clave, nombre)

                if (exitoso) {
                    _registroExitoso.value = true
                    _errorMensaje.value = null
                } else {
                    _errorMensaje.value = "Error al registrar usuario"
                }
            } catch (e: Exception) {
                _registroExitoso.value = false

                // Mensajes de error específicos
                _errorMensaje.value = when {
                    e.message?.contains("email address is already in use", ignoreCase = true) == true ->
                        "Este correo ya está registrado"
                    e.message?.contains("email address is badly formatted", ignoreCase = true) == true ->
                        "Formato de correo inválido"
                    e.message?.contains("password", ignoreCase = true) == true ->
                        "La contraseña es muy débil"
                    e.message?.contains("network", ignoreCase = true) == true ->
                        "Error de conexión. Verifica tu internet"
                    else ->
                        e.message ?: "Error al registrar usuario"
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    fun limpiarRegistro() {
        _registroExitoso.value = false
        _errorMensaje.value = null
    }

    fun limpiarError() {
        _errorMensaje.value = null
    }
}