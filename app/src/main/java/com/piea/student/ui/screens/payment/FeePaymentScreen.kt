package com.piea.student.ui.screens.payment

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Constants
import com.piea.student.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun FeePaymentScreen(
    applicationId: String,
    feeAmount: String,
    viewModel: FeePaymentViewModel = hiltViewModel(),
    onDone: () -> Unit
) {
    val context = LocalContext.current
    val submitState by viewModel.submitState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var receiptPicked by remember { mutableStateOf(false) }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            if (bytes != null) {
                receiptPicked = true
                viewModel.submitReceipt(applicationId, "fee_receipt_${System.currentTimeMillis()}", bytes)
            }
        }
    }

    LaunchedEffect(submitState) {
        when (val state = submitState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Receipt submitted! Our team will verify your payment shortly.") }
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { PieaTopBar("Application Fee") },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Application submitted!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "Please pay the processing fee below to move your application forward.",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    Text(
                        "Rs. ${feeAmount.ifBlank { "0" }}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("Pay via JazzCash", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            PaymentDetailCard(
                icon = Icons.Default.Payments,
                lines = listOf("JazzCash Number: ${Constants.JAZZCASH_NUMBER}")
            )

            Spacer(Modifier.height(14.dp))
            Text("Or pay via Bank Transfer", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            PaymentDetailCard(
                icon = Icons.Default.AccountBalance,
                lines = listOf(
                    "Bank: ${Constants.BANK_NAME}",
                    "Account Title: ${Constants.BANK_ACCOUNT_TITLE}",
                    "Account Number: ${Constants.BANK_ACCOUNT_NUMBER}"
                )
            )

            Spacer(Modifier.height(24.dp))

            if (submitState is Resource.Success) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Text(
                        "Receipt submitted — pending verification",
                        modifier = Modifier.padding(start = 8.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = onDone, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    Text("Continue to Application Tracking")
                }
            } else {
                Text(
                    "After paying, upload a screenshot of your payment receipt below.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Button(
                    onClick = { pickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = submitState !is Resource.Loading
                ) {
                    if (submitState is Resource.Loading) {
                        CircularProgressIndicator(modifier = Modifier.height(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Icon(Icons.Default.UploadFile, contentDescription = null)
                        Text("  Upload Payment Receipt")
                    }
                }
                Spacer(Modifier.height(10.dp))
                OutlinedButton(onClick = onDone, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                    Text("I'll pay later")
                }
            }
        }
    }
}

@Composable
private fun PaymentDetailCard(icon: androidx.compose.ui.graphics.vector.ImageVector, lines: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.padding(start = 12.dp)) {
                lines.forEach { line ->
                    Text(line, fontSize = 14.sp, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}
