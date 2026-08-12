package com.piea.student.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.navigation.Screen
import com.piea.student.ui.theme.PieaGradients

data class DashboardAction(val title: String, val icon: ImageVector, val route: String)

// Top 4 most-used actions shown as quick circular shortcuts (banking-app style)
private val quickActions = listOf(
    DashboardAction("Universities", Icons.Default.School, Screen.Universities.route),
    DashboardAction("Programs", Icons.Default.MenuBook, Screen.Programs.route),
    DashboardAction("Admission", Icons.Default.HowToReg, Screen.AdmissionForm.route),
    DashboardAction("Track", Icons.Default.Assignment, Screen.ApplicationTracking.route)
)

// Remaining services shown as a clean list (banking-app "All Services" style)
private val allServices = listOf(
    DashboardAction("Scholarships", Icons.Default.CardMembership, Screen.Scholarships.route),
    DashboardAction("Upload Documents", Icons.Default.CloudUpload, Screen.DocumentUpload.route),
    DashboardAction("Office Location", Icons.Default.LocationOn, Screen.OfficeLocation.route),
    DashboardAction("Help & Support", Icons.Default.Chat, Screen.HelpSupport.route)

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onActionClick: (String) -> Unit
) {
    val user by viewModel.user.collectAsState()
    val availableUpdate by viewModel.availableUpdate.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    availableUpdate?.let { update ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { viewModel.dismissUpdateDialog() },
            title = { Text("Update Available") },
            text = {
                Text(
                    "A new version (${update.latestVersionName}) of PIEA Student is available." +
                        if (update.releaseNotes.isNotBlank()) "\n\n${update.releaseNotes}" else ""
                )
            },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(update.downloadUrl))
                    context.startActivity(intent)
                    viewModel.dismissUpdateDialog()
                }) { Text("Update Now") }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { viewModel.dismissUpdateDialog() }) { Text("Later") }
            }
        )
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
        ) {
            // Top bar: avatar, greeting, notification bell
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text("Assalam-o-Alaikum", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        user?.fullName?.takeIf { it.isNotBlank() } ?: "Student",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { onActionClick(Screen.Notifications.route) }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.primary)
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {

                // Hero status card (banking "balance card" style)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PieaGradients.PrimaryHero)
                    ) {
                        // Decorative faint circle for a premium banking-card feel
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.06f))
                                .align(Alignment.TopEnd)
                                .offset(x = 30.dp, y = (-40).dp)
                        )
                        Column(modifier = Modifier.padding(22.dp)) {
                            Text(
                                "PIEA STUDENT PORTAL",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Your future starts here.",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                "Explore universities, apply for scholarships, and track your application status — all in one place.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Quick action shortcuts row (banking-app style circular icons)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    items(quickActions) { action ->
                        QuickActionShortcut(action) { onActionClick(action.route) }
                    }
                }

                Spacer(Modifier.height(26.dp))
                Text(
                    "All Services",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            // "All Services" clean list (banking-app style)
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                allServices.forEach { action ->
                    ServiceRow(action) { onActionClick(action.route) }
                }
            }
        }
    }
}

@Composable
private fun QuickActionShortcut(action: DashboardAction, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(76.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(PieaGradients.GoldAccent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(action.icon, contentDescription = action.title, tint = Color.White)
        }
        Text(
            action.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ServiceRow(action: DashboardAction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(action.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Text(
                action.title,
                modifier = Modifier.padding(start = 14.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}
