package com.example.calc3d

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calc3DApp()
        }
    }
}

@Composable
fun Calc3DApp() {
    val isDark = isSystemInDarkTheme()
    val colors = if (isDark) darkPalette else lightPalette

    MaterialTheme(
        colorScheme = colors
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            CalculatorScreen()
        }
    }
}

@Composable
fun CalculatorScreen() {
    var expr by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val expressionToShow = if (expr.isBlank()) "0" else expr

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            ThreeDDisplay(expressionToShow, result)
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            val rows = listOf(
                listOf("AC","(",")","/"),
                listOf("7","8","9","*"),
                listOf("4","5","6","-"),
                listOf("1","2","3","+"),
                listOf("0",".","⌫","=")
            )
            for (row in rows) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (label in row) {
                        Box(modifier = Modifier
                            .weight(1f)
                            .height(74.dp)
                        ) {
                            CalcButton(label = label, onClick = {
                                when(label) {
                                    "AC" -> { expr = ""; result = "" }
                                    "⌫" -> { if (expr.isNotEmpty()) expr = expr.dropLast(1) }
                                    "=" -> {
                                        val r = try { evalExpression(expr) } catch (e: Exception) { Double.NaN }
                                        result = if (r.isFinite()) formatDouble(r) else "Error"
                                    }
                                    else -> {
                                        expr += label
                                    }
                                }
                            })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun ThreeDDisplay(expression: String, result: String) {
    val tilt = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while(true) {
            tilt.animateTo(8f, animationSpec = tween(1200, easing = FastOutSlowInEasing))
            tilt.animateTo(-6f, animationSpec = tween(1600, easing = FastOutSlowInEasing))
            delay(800)
        }
    }

    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .graphicsLayer {
                rotationX = tilt.value
                cameraDistance = 12f * density
                shadowElevation = 22f
            }
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(colors.primary.copy(alpha = 0.12f), colors.background)
                )
            )
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = expression,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                modifier = Modifier.align(Alignment.End),
                color = colors.onBackground
            )
            Text(
                text = if (result.isBlank()) "" else "= $result",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.align(Alignment.End),
                color = colors.primary
            )
        }
    }
}

@Composable
fun CalcButton(label: String, onClick: ()->Unit) {
    val colors = MaterialTheme.colorScheme
    var pressed by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(targetValue = if (pressed) 0.96f else 1f, animationSpec = tween(120))
    val elevation = if (pressed) 4.dp else 12.dp

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                },
                onTap = { onClick() }
            )
        }
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .shadow(elevation, shape = RoundedCornerShape(18.dp))
        .clip(RoundedCornerShape(18.dp))
        .background(
            Brush.verticalGradient(listOf(
                if (pressed) colors.surfaceVariant else colors.surface,
                colors.surface
            ))
        )
        .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
            color = when(label) {
                "/", "*", "-", "+", "=" -> colors.primary
                "AC","⌫" -> colors.error
                else -> colors.onSurface
            }
        )
    }
}
