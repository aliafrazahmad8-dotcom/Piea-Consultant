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
import com.piea.student.data.model.Scholarship
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun AdminAddScholarshipScreen(
    viewModel: AdminAddScholarshipViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var provider by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var coveragePercent by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var eligibility by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val addState by viewModel.addState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(addState) {
        when (val state = addState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Scholarship added successfully!") }
                title = ""; provider = ""; country = ""; coveragePercent = ""
                deadline = ""; eligibility = ""; description = ""
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
        topBar = { PieaTopBar("Admin — Add Scholarship", onBack) },
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
                "This scholarship will immediately appear for students in the Scholarships list.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Field("Scholarship Title *", title) { title = it }
            Field("Provider *", provider) { provider = it }
            Field("Country", country) { country = it }
            Field("Coverage (e.g. 100% Tuition)", coveragePercent) { coveragePercent = it }
            Field("Deadline (e.g. 30 Sep 2026)", deadline) { deadline = it }
            Field("Eligibility", eligibility) { eligibility = it }
            Field("Description", description) { description = it }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.addScholarship(
                        Scholarship(
                            title = title, provider = provider, country = country,
                            coveragePercent = coveragePercent, deadline = deadline,
                            eligibility = eligibility, description = description
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = addState !is Resource.Loading
            ) {
                if (addState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.height(22.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Add Scholarship", fontSize = 16.sp)
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
