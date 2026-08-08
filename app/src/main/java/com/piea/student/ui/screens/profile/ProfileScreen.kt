package com.piea.student.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.piea.student.ui.components.ErrorView
import com.piea.student.ui.components.LoadingView
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onOpenSettings: () -> Unit,
    onOpenAdmin: () -> Unit,
    onLoggedOut: () -> Unit
) {
    val profileState by viewModel.profileState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    val currentEmail = FirebaseAuth.getInstance().currentUser?.email
    val isAdmin = currentEmail != null && currentEmail.equals(Constants.ADMIN_EMAIL, ignoreCase = true)

    Scaffold(topBar = { PieaTopBar("My Profile") }) { padding ->
        when (val state = profileState) {
            is Resource.Loading -> LoadingView(Modifier.padding(padding))
            is Resource.Error -> ErrorView(state.message, Modifier.padding(padding))
            is Resource.Success -> {
                var fullName by remember(state.data.uid) { mutableStateOf(state.data.fullName) }
                var phone by remember(state.data.uid) { mutableStateOf(state.data.phoneNumber) }
                var address by remember(state.data.uid) { mutableStateOf(state.data.address) }
                var cnic by remember(state.data.uid) { mutableStateOf(state.data.cnic) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(44.dp))
                        }
                        Text(state.data.fullName.ifBlank { "Student" }, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 10.dp))
                        Text(state.data.email, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, enabled = editMode, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, enabled = editMode, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
                    OutlinedTextField(value = cnic, onValueChange = { cnic = it }, label = { Text("CNIC / Passport") }, enabled = editMode, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address") }, enabled = editMode, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp))

                    if (editMode) {
                        Button(
                            onClick = {
                                viewModel.updateProfile(state.data.copy(fullName = fullName, phoneNumber = phone, address = address, cnic = cnic))
                                editMode = false
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) { Text("Save Changes") }
                    } else {
                        OutlinedButton(onClick = { editMode = true }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                            Text("Edit Profile")
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Card(shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)) {
                        SettingsRow(icon = Icons.Default.Settings, label = "App Settings", onClick = onOpenSettings)
                    }

                    if (isAdmin) {
                        Spacer(Modifier.height(12.dp))
                        Card(shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)) {
                            SettingsRow(icon = Icons.Default.AdminPanelSettings, label = "Admin Panel", onClick = onOpenAdmin)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text("  Logout", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            else -> Unit
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout of PIEA Student?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                    onLoggedOut()
                }) { Text("Logout") }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(label, modifier = Modifier.padding(start = 12.dp), fontWeight = FontWeight.Medium)
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}
