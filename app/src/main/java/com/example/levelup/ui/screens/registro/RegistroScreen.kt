package com.example.levelup.ui.screens.registro

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.R

@Composable
fun RegistroScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

    val viewModel: com.example.levelup.viewmodel.RegistroViewModel = viewModel()
    val cargando by viewModel.cargando.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()
    val errorMensaje by viewModel.errorMensaje.collectAsState()

    // Observar éxito del registro
    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            Toast.makeText(context, "¡Registro exitoso! Ahora inicia sesión", Toast.LENGTH_LONG).show()
            viewModel.limpiarRegistro()
            onRegisterSuccess()
        }
    }

    // Observar errores y mostrar Toast
    LaunchedEffect(errorMensaje) {
        errorMensaje?.let { mensaje ->
            if (mensaje.isNotEmpty()) {
                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                viewModel.limpiarError() // Limpiar después de mostrar
            }
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
                    bottom = 24.dp)
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

            // Campo NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = {
                    Text(
                        "NOMBRE COMPLETO",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00FFAA)
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

            // Campo CORREO
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = {
                    Text(
                        "CORREO",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00FFAA)
                        )
                    )
                },
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

            // Campo CONTRASEÑA
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = {
                    Text(
                        "CONTRASEÑA",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00FFAA)
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

            Spacer(Modifier.height(16.dp))

            // Campo CONFIRMAR CONTRASEÑA
            OutlinedTextField(
                value = confirmarClave,
                onValueChange = { confirmarClave = it },
                label = {
                    Text(
                        "CONFIRMAR CONTRASEÑA",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00FFAA)
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

            // Botón REGISTRARSE
            Button(
                onClick = {
                    viewModel.registroUsuario(correo, clave, confirmarClave, nombre)
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
                enabled = !cargando
            ) {
                if (cargando) {
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

            // Enlace para volver
            Spacer(Modifier.height(16.dp))

            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            TextButton(
                onClick = onBack,
                interactionSource = interactionSource
            ) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión aquí",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isPressed) Color(0xFFAAAAAA) else Color(0xFF888888),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}