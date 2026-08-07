package com.piea.student.ui.screens.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.piea.student.ui.components.PieaTopBar

private val OfficeLatLng = LatLng(31.5204, 74.3587) // Lahore, Punjab — update with the real PIEA office coordinates

@Composable
fun OfficeLocationScreen(onBack: () -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(OfficeLatLng, 15f)
    }

    Scaffold(topBar = { PieaTopBar("Office Location", onBack) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxWidth().weight(1f),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = OfficeLatLng),
                    title = "PIEA Head Office",
                    snippet = "Lahore, Punjab, Pakistan"
                )
            }
            Column(Modifier.padding(16.dp)) {
                androidx.compose.foundation.layout.Row {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "PIEA Head Office",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    "Lahore, Punjab, Pakistan",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, start = 32.dp)
                )
            }
        }
    }
}
