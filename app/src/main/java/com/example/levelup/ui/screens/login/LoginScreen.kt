package com.example.levelup.ui.screens.login

import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.levelup.R

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.repository.AuthRepository
import com.example.levelup.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: (User: com.example.levelup.model.User) -> Unit = {}
){ //Función de inicio sesión
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val viewModel: LoginViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    val carga by viewModel.cargaLogin.collectAsState()
    val error by viewModel.error.collectAsState()

    //Variable de conexión al Auth
    val repositorio = AuthRepository()

    LaunchedEffect(key1 = user) {
        if (user != null) {
            val mensaje = when (user?.rol) {
                "admin" -> "Bienvenido Admin: ${user?.nombre}"
                else -> "Bienvenido: ${user?.nombre}"
            }
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            onLoginSuccess(user!!)
        }
    }

    LaunchedEffect(key1 = error) {
        error?.let { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    var showEmptyFieldsError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = showEmptyFieldsError) {
        if (showEmptyFieldsError) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            showEmptyFieldsError = false
        }
    }

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
                    bottom = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp)
            )

            Text("INICIAR SESIÓN",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF00FFAA) // Verde cyber
                )
            )

            // Campo USUARIO
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = {
                    Text(
                        "CORREO",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00FFAA) // Label verde
                        )
                    )
                },
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

            // Campo CONTRASEÑA
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = {
                    Text(
                        "CONTRASEÑA",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00FFAA) // Label verde
                        )
                    )
                },
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

            // Botón INGRESAR
            Button(
                onClick = {
                    if (correo.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(context, "Completar todos los datos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    viewModel.login(correo, pass)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00FFAA),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xFF00FFAA),
                    disabledContentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                ),
                enabled = !carga
            )
            {
                if (carga) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Gray,
                        strokeWidth = 3.dp
                    )
                } else {
                Text(
                    "INGRESAR",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black
                    )
                )
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onRegisterClick) {
                Text(
                    "¿No tienes cuenta? Regístrate aquí",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF888888),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}