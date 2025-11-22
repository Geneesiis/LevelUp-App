package com.example.levelup.repository

import com.example.levelup.data.local.UserDao
import com.example.levelup.model.User

class AuthRepository(private val userDao: UserDao) {

    // Obtener usuario actual (desde SharedPreferences o variable en memoria)
    private var usuarioActualId: String? = null

    suspend fun getUsuarioActual(): User? {
        return usuarioActualId?.let { userDao.getUserById(it) }
    }

    // Login usando Room
    suspend fun login(correo: String, clave: String): User {
        // Buscar usuario por correo
        val usuario = userDao.getUserByEmail(correo)
            ?: throw Exception("El correo no está registrado")

        // Verificar contraseña (en producción deberías usar hash)
        if (usuario.password != clave) {
            throw Exception("Contraseña incorrecta")
        }

        // Guardar ID del usuario actual
        usuarioActualId = usuario.id

        return usuario
    }

    // Registro de nuevo usuario en Room
    suspend fun registro(nombre: String, correo: String, clave: String): User {
        // Verificar si el correo ya existe
        val existente = userDao.getUserByEmail(correo)
        if (existente != null) {
            throw Exception("Este correo ya está registrado")
        }

        // Validar contraseña
        if (clave.length < 6) {
            throw Exception("La contraseña debe tener al menos 6 caracteres")
        }

        // Crear nuevo usuario
        val nuevoUsuario = User(
            id = java.util.UUID.randomUUID().toString(),
            email = correo,
            nombre = nombre,
            password = clave, // En producción deberías usar hash
            isAdmin = false
        )

        // Insertar en Room
        userDao.insert(nuevoUsuario)

        return nuevoUsuario
    }

    // Cerrar sesión
    fun logout() {
        usuarioActualId = null
    }

    // Verificar si hay usuario autenticado
    fun isUserLoggedIn(): Boolean {
        return usuarioActualId != null
    }
}