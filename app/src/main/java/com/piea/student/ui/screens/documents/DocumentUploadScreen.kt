package com.piea.student.ui.screens.documents

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun DocumentUploadScreen(
    viewModel: DocumentsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val documentsState by viewModel.documents.collectAsState()
    val uploadState by viewModel.uploadState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedDocType by remember { mutableStateOf("") }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && selectedDocType.isNotBlank()) {
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            val fileName = selectedDocType.replace(" ", "_") + "_" + System.currentTimeMillis()
            if (bytes != null) {
                viewModel.uploadDocument(selectedDocType, fileName, bytes)
            }
        }
    }

    LaunchedEffect(uploadState) {
        when (val state = uploadState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("${state.data.documentType} uploaded successfully.") }
                viewModel.resetUploadState()
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                viewModel.resetUploadState()
            }
            else -> Unit
        }
    }

    val uploadedTypes = (documentsState as? Resource.Success)?.data?.map { it.documentType }?.toSet() ?: emptySet()

    Scaffold(
        topBar = { PieaTopBar("Document Upload", onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Text(
                "Upload the required documents for your application. Accepted formats: PDF, JPG, PNG.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)) {
                items(Constants.REQUIRED_DOCUMENTS) { docType ->
                    val uploaded = uploadedTypes.contains(docType)
                    val isUploadingThis = uploadState is Resource.Loading && selectedDocType == docType

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clickable(enabled = !isUploadingThis) {
                                selectedDocType = docType
                                pickerLauncher.launch("*/*")
                            },
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Description,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(docType, fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.padding(start = 12.dp))
                            }
                            when {
                                isUploadingThis -> CircularProgressIndicator(modifier = Modifier.padding(2.dp).size(20.dp))
                                uploaded -> Icon(Icons.Default.CheckCircle, contentDescription = "Uploaded", tint = MaterialTheme.colorScheme.secondary)
                                else -> Icon(Icons.Default.UploadFile, contentDescription = "Upload", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
