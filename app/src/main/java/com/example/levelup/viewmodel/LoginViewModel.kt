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
    var error: StateFlow<String?> = _error

    fun login (correo: String, clave: String) {
        _cargaLogin.value = true
        viewModelScope.launch {
            try {
                _user.value = repositorio.login(correo, clave)
            } catch (e: Exception) {
                _user.value = null
            } finally {
                _cargaLogin.value = false
            }
        }
    }
}