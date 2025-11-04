package com.example.levelup.repository

import com.example.levelup.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(correo: String, clave: String): User? {
        return try {
            // Intentar autenticar con Firebase
            val resultado = auth.signInWithEmailAndPassword(correo, clave).await()
            val firebaseUser = resultado.user

            if (firebaseUser != null) {
                // Para el admin especial
                if (correo == "admin@levelup.cl") {
                    User(
                        correo = correo,
                        nombre = "Administrador",
                        rol = "admin"
                    )
                } else {
                    // Para usuarios normales, obtener datos de Firestore
                    obtenerUsuarioDeFirestore(firebaseUser.uid, correo)
                }
            } else {
                throw Exception("No se pudo obtener el usuario")
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            // El usuario no existe (correo no registrado)
            throw Exception("El correo no está registrado")

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // La contraseña es incorrecta
            throw Exception("Contraseña incorrecta")

        } catch (e: Exception) {
            // Otros errores
            when {
                e.message?.contains("There is no user record", ignoreCase = true) == true ->
                    throw Exception("El correo no está registrado")
                e.message?.contains("password is invalid", ignoreCase = true) == true ->
                    throw Exception("Contraseña incorrecta")
                e.message?.contains("network", ignoreCase = true) == true ->
                    throw Exception("Error de conexión. Verifica tu internet")
                else ->
                    throw Exception(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    private suspend fun obtenerUsuarioDeFirestore(uid: String, correo: String): User? {
        return try {
            val query = db.collection("Usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            if (!query.isEmpty && query.documents.isNotEmpty()) {
                val doc = query.documents[0]
                User(
                    correo = doc.getString("correo") ?: "",
                    nombre = doc.getString("nombre") ?: "Cliente",
                    rol = doc.getString("rol") ?: "cliente"
                )
            } else {
                User(
                    correo = correo,
                    nombre = "Usuario",
                    rol = "cliente"
                )
            }
        } catch (e: Exception) {
            User(
                correo = correo,
                nombre = "Usuario",
                rol = "cliente"
            )
        }
    }

    //Registro de nuevo usuario
    suspend fun registrar(nombre: String, correo: String, clave: String): User? {
        return try {
            // Crear usuario en Firebase Auth
            val resultado = auth.createUserWithEmailAndPassword(correo, clave).await()
            val uid = resultado.user?.uid ?: throw Exception("Error al crear usuario")

            // Guardar datos en Firestore
            val userData = hashMapOf(
                "nombre" to nombre,
                "correo" to correo,
                "rol" to "cliente"
            )

            db.collection("Usuario")
                .document(uid)
                .set(userData)
                .await()

            User(
                correo = correo,
                nombre = nombre,
                rol = "cliente"
            )
        } catch (e: Exception) {
            throw Exception("Error al registrar: ${e.message}")
        }
    }

    //Cerrar sesión
    fun logout() {
        auth.signOut()
    }
}