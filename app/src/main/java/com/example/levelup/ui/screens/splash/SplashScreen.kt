import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.levelup.R
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun SplashSplash() {
    // --- Animaciones ---
    val scale = remember { Animatable(0.5f) }   // comienza con scale(.5)
    val alpha = remember { Animatable(0f) }     // comienza invisible

    // --- Lanzamos las animaciones ---
    LaunchedEffect(Unit) {
        // Escala del logo
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = CubicBezierEasing(0.39f, 0.575f, 0.565f, 1.0f)
            )
        )
    }

    LaunchedEffect(Unit) {
        // Fade in paralelo
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = LinearEasing
            )
        )
        // Espera antes de continuar (opcional)
        delay(1000)
    }

    // --- UI del Splash ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // tu logo aquí
            contentDescription = "App Logo",
            modifier = Modifier
                .size(150.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashSplash()
}
