package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.User
import com.example.levelup.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repositorio = AuthRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _cargaLogin = MutableStateFlow(false)
    val cargaLogin: StateFlow<Boolean> = _cargaLogin

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(correo: String, clave: String) {
        // Limpiar error anterior
        _error.value = null
        _cargaLogin.value = true

        viewModelScope.launch {
            try {
                val usuario = repositorio.login(correo, clave)
                _user.value = usuario
                _error.value = null
            } catch (e: Exception) {
                _user.value = null

                // Capturar el mensaje de error y mostrarlo
                _error.value = when {
                    e.message?.contains("password", ignoreCase = true) == true ->
                        "Contraseña incorrecta"
                    e.message?.contains("user", ignoreCase = true) == true ||
                            e.message?.contains("found", ignoreCase = true) == true ->
                        "Usuario no encontrado"
                    e.message?.contains("There is no user record", ignoreCase = true) == true ->
                        "El correo no está registrado"
                    e.message?.contains("password is invalid", ignoreCase = true) == true ->
                        "Contraseña incorrecta"
                    e.message?.contains("network", ignoreCase = true) == true ->
                        "Error de conexión. Verifica tu internet"
                    else ->
                        e.message ?: "Error al iniciar sesión"
                }
            } finally {
                _cargaLogin.value = false
            }
        }
    }

    /**
     * Limpia el error después de mostrarlo
     */
    fun limpiarError() {
        _error.value = null
    }

    /**
     * Cierra sesión
     */
    fun logout() {
        _user.value = null
        _error.value = null
    }
}