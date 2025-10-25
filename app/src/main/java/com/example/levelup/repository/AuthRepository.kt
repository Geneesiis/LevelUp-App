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
            val resultado = auth.signInWithEmailAndPassword(correo, clave).await()
            val user = resultado.user
            if (user != null) {
                getUserFromFirestore(user.uid, correo) ?: User (
                    correo = correo,
                    nombre = if (correo == "admin@levelup.cl") "Administrador" else "User",
                    rol = if (correo == "admin@levelup.cl") "admin" else "cliente"
                )
            } else null
        } catch (e: Exception) {
            //Si falla Auth, intentar con Firestore
            loginWithFirestore(correo, clave)
        }
    }

    private suspend fun getUserFromFirestore (uid: String, correo: String): User? {
        return try {
            val documento = db.collection("Usuario").document(uid).get().await()
            if (documento.exists()) {
                User(
                    correo = documento.getString("correo") ?: correo,
                    nombre = documento.getString("nombre") ?: "Usuario",
                    rol = documento.getString("rol") ?: "cliente"
                )
            } else null
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

            if (!query.isEmpty) {
                val doc = query.documents[0]
                User (
                    correo = doc.getString("correo") ?: "",
                    nombre = doc.getString("nombre") ?: "Cliente",
                    rol = doc.getString("rol") ?: "cliente"
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}