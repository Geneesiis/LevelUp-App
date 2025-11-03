package com.example.levelup.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class UsuarioRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun registroUsuario(correo: String, clave: String, nombre: String): Boolean {
        return try {
            // Intentar crear usuario en Firebase Auth
            val resultado = auth.createUserWithEmailAndPassword(correo, clave).await()
            val user = resultado.user

            if (user != null) {
                // Guardar datos adicionales en Firestore
                val userData = hashMapOf(
                    "correo" to correo,
                    "nombre" to nombre,
                    "rol" to "cliente",
                    "fechaRegistro" to getCurrentDate()
                )

                db.collection("Usuario").document(user.uid).set(userData).await()
                true
            } else {
                throw Exception("Error al crear el usuario")
            }

        } catch (e: FirebaseAuthUserCollisionException) {
            // El correo ya está registrado
            throw Exception("Este correo ya está registrado")

        } catch (e: FirebaseAuthWeakPasswordException) {
            // Contraseña muy débil (menos de 6 caracteres)
            throw Exception("La contraseña debe tener al menos 6 caracteres")

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Formato de correo inválido
            throw Exception("El formato del correo es inválido")

        } catch (e: Exception) {
            // Otros errores de Firebase o red
            when {
                e.message?.contains("email address is already in use", ignoreCase = true) == true ->
                    throw Exception("Este correo ya está registrado")
                e.message?.contains("email address is badly formatted", ignoreCase = true) == true ->
                    throw Exception("El formato del correo es inválido")
                e.message?.contains("network", ignoreCase = true) == true ->
                    throw Exception("Error de conexión. Verifica tu internet")
                else ->
                    throw Exception(e.message ?: "Error al registrar usuario")
            }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}