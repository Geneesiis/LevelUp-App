package com.example.levelup.repository

import com.example.levelup.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun login(correo: String, clave: String) : User? {
        return try {
            //Intentar autenticar con autenticación de Firebase - admin
            when {
                correo == "admin@levelup.cl" -> {
                    val resultado = auth.signInWithEmailAndPassword(correo, clave).await()
                    User (
                        correo = correo,
                        nombre = "Administrador",
                        rol = "admin"
                    )
                }
                else -> {
                    loginWithFirestore(correo, clave)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun loginWithFirestore(correo: String, clave: String): User? {
        return try {
            val query = db.collection("Usuario")
                .whereEqualTo("correo", correo)
                .whereEqualTo("clave", clave)
                .get()
                .await()

            if (!query.isEmpty && query.documents.isNotEmpty()) {
                val doc = query.documents[0]
                User (
                    correo = doc.getString("correo") ?: "",
                    clave = doc.getString("clave") ?: "",
                    nombre = doc.getString("nombre") ?: "Cliente",
                    rol = doc.getString("rol") ?: "cliente"
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}