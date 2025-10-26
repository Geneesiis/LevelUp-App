package com.example.levelup.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class UsuarioRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun registroUsuario(correo: String, clave: String, nombre: String): Boolean {
        return try {
            val resultado = auth.createUserWithEmailAndPassword(correo, clave).await()
            val user = resultado.user

            if (user != null) {
                val userData = hashMapOf(
                    "correo" to correo,
                    "nombre" to nombre,
                    "rol" to "cliente",
                    "fechaRegistro" to getCurrentDate()
                )

                db.collection("Usuario").document(user.uid).set(userData).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}