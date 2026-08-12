package com.piea.student.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piea.student.ui.theme.PieaGradients
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: (Boolean) -> Unit, isLoggedIn: Boolean) {
    LaunchedEffect(Unit) {
        delay(900)
        onFinished(isLoggedIn)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PieaGradients.SplashBackground),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .height(120.dp)
                .padding(bottom = 4.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.14f))
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.height(56.dp)
            )
        }
        Text(
            "PIEA",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, bottom = 6.dp)
        )
        Text(
            "PAK INTERNATIONAL EDUCATIONAL ADVISOR",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
        )
        CircularProgressIndicator(color = Color.White, modifier = Modifier.height(28.dp))
    }
}
