package com.example.levelup.ui.screens.registro

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.levelup.R
import com.example.levelup.model.User
import com.example.levelup.viewmodel.AuthState
import com.example.levelup.viewmodel.AuthViewModel

@Composable
fun RegistroScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: (User) -> Unit
) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Authenticated -> {
                Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_LONG).show()
                onRegisterSuccess(state.user)
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                authViewModel.clearError()
            }
            else -> Unit
        }
    }

    val isLoading = authState is AuthState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 32.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                "CREAR CUENTA",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00FFAA)
                )
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("NOMBRE COMPLETO", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF00FFAA))) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF111111),
                    unfocusedContainerColor = Color(0xFF111111),
                    focusedIndicatorColor = Color(0xFF00FFAA),
                    unfocusedIndicatorColor = Color(0xFF666666),
                    cursorColor = Color(0xFF00FFAA)
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("CORREO", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF00FFAA))) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF111111),
                    unfocusedContainerColor = Color(0xFF111111),
                    focusedIndicatorColor = Color(0xFF00FFAA),
                    unfocusedIndicatorColor = Color(0xFF666666),
                    cursorColor = Color(0xFF00FFAA)
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("CONTRASEÑA", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF00FFAA))) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF111111),
                    unfocusedContainerColor = Color(0xFF111111),
                    focusedIndicatorColor = Color(0xFF00FFAA),
                    unfocusedIndicatorColor = Color(0xFF666666),
                    cursorColor = Color(0xFF00FFAA)
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmarClave,
                onValueChange = { confirmarClave = it },
                label = { Text("CONFIRMAR CONTRASEÑA", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF00FFAA))) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF111111),
                    unfocusedContainerColor = Color(0xFF111111),
                    focusedIndicatorColor = Color(0xFF00FFAA),
                    unfocusedIndicatorColor = Color(0xFF666666),
                    cursorColor = Color(0xFF00FFAA)
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || correo.isBlank() || clave.isBlank()) {
                        Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (clave != confirmarClave) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    authViewModel.register(nombre, correo, clave)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FFAA),
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.DarkGray
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        "REGISTRARSE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onBack) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión aquí",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF888888),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
