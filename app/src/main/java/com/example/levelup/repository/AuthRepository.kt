package com.example.levelup.repository

import com.example.levelup.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(correo: String, clave: String): User? {
        return try {
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
                null
            }
        } catch (e: Exception) {
            throw Exception("Error de autenticación: ${e.message}")
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
}