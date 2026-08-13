package com.piea.student.ui.screens.support

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource
import kotlinx.coroutines.launch

/**
 * Shared screen for both Feedback and Complaint — same form shape, different
 * title/type sent to Firestore so PIEA staff can filter by type.
 */
@Composable
fun SupportFormScreen(
    type: String, // "Feedback" or "Complaint"
    viewModel: SupportFormViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val submitState by viewModel.submitState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(submitState) {
        if (submitState is Resource.Error) {
            scope.launch { snackbarHostState.showSnackbar((submitState as Resource.Error).message) }
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = { PieaTopBar(type, onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            if (submitState is Resource.Success) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.height(56.dp))
                    Text(
                        "Thank you! Your $type has been sent.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        "Our team will review it shortly.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Text(
                    if (type == "Complaint") "Tell us what went wrong — we'll do our best to resolve it quickly."
                    else "We'd love to hear your thoughts on the PIEA Student app.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Message *") },
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                Button(
                    onClick = { viewModel.submit(type, subject, message) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = submitState !is Resource.Loading
                ) {
                    if (submitState is Resource.Loading) {
                        CircularProgressIndicator(modifier = Modifier.height(22.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Submit $type", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
