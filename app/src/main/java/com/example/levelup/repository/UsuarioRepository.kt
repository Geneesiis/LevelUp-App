package com.example.levelup.repository

import com.example.levelup.data.local.UserDao
import com.example.levelup.model.User

class UsuarioRepository(private val userDao: UserDao) {

    // Registrar usuario en Room (llamado desde AuthViewModel)
    suspend fun registroUsuario(usuario: User) {
        try {
            userDao.insert(usuario)
        } catch (e: Exception) {
            throw Exception("Error al guardar usuario: ${e.message}")
        }
    }

    // Obtener usuario por ID
    suspend fun obtenerUsuario(userId: String): User? {
        return try {
            userDao.getUserById(userId)
        } catch (e: Exception) {
            null
        }
    }

    // Obtener usuario por email
    suspend fun obtenerUsuarioPorEmail(email: String): User? {
        return try {
            userDao.getUserByEmail(email)
        } catch (e: Exception) {
            null
        }
    }

    // Obtener todos los usuarios
    suspend fun obtenerTodosLosUsuarios(): List<User> {
        return try {
            userDao.getAllUsers()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Actualizar usuario
    suspend fun actualizarUsuario(usuario: User) {
        try {
            userDao.update(usuario)
        } catch (e: Exception) {
            throw Exception("Error al actualizar usuario: ${e.message}")
        }
    }

    // Eliminar usuario
    suspend fun eliminarUsuario(usuario: User) {
        try {
            userDao.delete(usuario)
        } catch (e: Exception) {
            throw Exception("Error al eliminar usuario: ${e.message}")
        }
    }

    suspend fun borrarTodosLosUsuarios() {
        userDao.deleteAllUsers()
    }
}