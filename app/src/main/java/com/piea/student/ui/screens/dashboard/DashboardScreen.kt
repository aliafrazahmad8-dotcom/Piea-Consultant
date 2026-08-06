package com.piea.student.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.navigation.Screen

data class DashboardAction(val title: String, val icon: ImageVector, val route: String)

private val actions = listOf(
    DashboardAction("Universities", Icons.Default.School, Screen.Universities.route),
    DashboardAction("Scholarships", Icons.Default.CardMembership, Screen.Scholarships.route),
    DashboardAction("Programs", Icons.Default.MenuBook, Screen.Programs.route),
    DashboardAction("Admission Form", Icons.Default.HowToReg, Screen.AdmissionForm.route),
    DashboardAction("Upload Documents", Icons.Default.CloudUpload, Screen.DocumentUpload.route),
    DashboardAction("Track Application", Icons.Default.Assignment, Screen.ApplicationTracking.route),
    DashboardAction("Office Location", Icons.Default.LocationOn, Screen.OfficeLocation.route),
    DashboardAction("WhatsApp Support", Icons.Default.Chat, Screen.WhatsAppSupport.route)
)

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onActionClick: (String) -> Unit
) {
    val user by viewModel.user.collectAsState()

    Scaffold { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            Text(
                text = "Assalam-o-Alaikum,",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = (user?.fullName?.takeIf { it.isNotBlank() } ?: "Student").plus(" 👋"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "Your future starts here.",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "Explore universities abroad, apply for scholarships, and track your application — all in one place.",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            Text(
                "Quick Actions",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    DashboardActionCard(action) { onActionClick(action.route) }
                }
            }
        }
    }
}

@Composable
private fun DashboardActionCard(action: DashboardAction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1.4f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(action.icon, contentDescription = action.title, tint = MaterialTheme.colorScheme.primary)
            Text(
                action.title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
