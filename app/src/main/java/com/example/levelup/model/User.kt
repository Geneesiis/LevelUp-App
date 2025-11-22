package com.example.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String = "",
    val email: String = "",
    val nombre: String = "",
    val password: String = "",
    val isAdmin: Boolean = false
)