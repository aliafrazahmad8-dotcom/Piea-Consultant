package com.piea.student.ui.screens.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun AdminPublishUpdateScreen(
    viewModel: AdminPublishUpdateViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var versionCode by remember { mutableStateOf("") }
    var versionName by remember { mutableStateOf("") }
    var downloadUrl by remember { mutableStateOf("") }
    var releaseNotes by remember { mutableStateOf("") }

    val publishState by viewModel.publishState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(publishState) {
        when (val state = publishState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Update published! Students will be notified next time they open the app.") }
                viewModel.resetState()
            }
            is Resource.Error -> {
                scope.launch { snackbarHostState.showSnackbar(state.message) }
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = { PieaTopBar("Admin — Publish Update", onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                "Steps: 1) Build the new APK on GitHub Actions. 2) Create a GitHub Release and attach the APK there (gives a permanent public link). 3) Paste that link below and publish — every student will see an update prompt next time they open the app.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Field("New Version Code * (e.g. 2 — must be higher than last time)", versionCode) { versionCode = it }
            Field("New Version Name * (e.g. 1.1.0)", versionName) { versionName = it }
            Field("APK Download Link *", downloadUrl) { downloadUrl = it }
            Field("What's New (optional)", releaseNotes) { releaseNotes = it }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.publishUpdate(versionCode, versionName, downloadUrl, releaseNotes) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = publishState !is Resource.Loading
            ) {
                if (publishState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.height(22.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Publish Update", fontSize = 16.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    )
}
