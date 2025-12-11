package com.example.levelup.repository

import com.example.levelup.data.local.UserDao
import com.example.levelup.data.session.SessionManager
import com.example.levelup.model.User

class AuthRepository(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) {

    suspend fun getUsuarioActual(): User? {
        val userId = sessionManager.getUserId()
        return userId?.let { userDao.getUserById(it) }
    }

    suspend fun login(correo: String, clave: String): User {
        val usuario = userDao.getUserByEmail(correo)
            ?: throw Exception("El correo no está registrado")

        if (usuario.password != clave) {
            throw Exception("Contraseña incorrecta")
        }

        sessionManager.saveSession(usuario)

        return usuario
    }

    suspend fun registro(nombre: String, correo: String, clave: String): User {
        val existente = userDao.getUserByEmail(correo)
        if (existente != null) {
            throw Exception("Este correo ya está registrado")
        }

        if (clave.length < 6) {
            throw Exception("La contraseña debe tener al menos 6 caracteres")
        }

        val esAdmin = correo.equals("admin@levelup.com", ignoreCase = true)

        val nuevoUsuario = User(
            id = java.util.UUID.randomUUID().toString(),
            email = correo,
            nombre = nombre,
            password = clave, // En producción deberías usar hash
            isAdmin = esAdmin
        )

        userDao.insert(nuevoUsuario)

        // Iniciar sesión después del registro
        sessionManager.saveSession(nuevoUsuario)

        return nuevoUsuario
    }

    suspend fun logout() {
        sessionManager.clearSession()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return sessionManager.isLoggedIn()
    }

    suspend fun updateUser(nombre: String, email: String, contrasena: String) {
        val usuarioActual = getUsuarioActual()
            ?: throw Exception("No se pudo encontrar al usuario actual para actualizar.")

        val usuarioActualizado = usuarioActual.copy(
            nombre = nombre,
            email = email,
            password = if (contrasena.isNotBlank()) {
                if (contrasena.length < 6) throw Exception("La contraseña debe tener al menos 6 caracteres")
                contrasena // En producción deberías usar hash
            } else {
                usuarioActual.password
            }
        )
        userDao.update(usuarioActualizado)
    }

    suspend fun cleanDatabase() {
        userDao.deleteAllUsers()
    }
}