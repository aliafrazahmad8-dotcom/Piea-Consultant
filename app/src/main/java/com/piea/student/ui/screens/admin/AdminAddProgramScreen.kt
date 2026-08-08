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
import com.piea.student.data.model.Program
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun AdminAddProgramScreen(
    viewModel: AdminAddProgramViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var universityName by remember { mutableStateOf("") }
    var degreeLevel by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var tuitionFee by remember { mutableStateOf("") }
    var applicationFee by remember { mutableStateOf("") }
    var intake by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val addState by viewModel.addState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(addState) {
        when (val state = addState) {
            is Resource.Success -> {
                scope.launch { snackbarHostState.showSnackbar("Program added successfully!") }
                title = ""; universityName = ""; degreeLevel = ""; duration = ""
                tuitionFee = ""; applicationFee = ""; intake = ""; description = ""
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
        topBar = { PieaTopBar("Admin — Add Program", onBack) },
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
                "This program (and its application fee) will immediately appear for students in the Admission Form and Programs list.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Field("Program Title *", title) { title = it }
            Field("University Name *", universityName) { universityName = it }
            Field("Degree Level (Bachelor / Master / PhD / Diploma)", degreeLevel) { degreeLevel = it }
            Field("Duration (e.g. 4 Years)", duration) { duration = it }
            Field("Tuition Fee (per year)", tuitionFee) { tuitionFee = it }
            Field("Application Processing Fee (Rs.) *", applicationFee) { applicationFee = it }
            Field("Intake (e.g. Fall 2026)", intake) { intake = it }
            Field("Description", description) { description = it }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.addProgram(
                        Program(
                            title = title,
                            universityName = universityName,
                            degreeLevel = degreeLevel,
                            duration = duration,
                            tuitionFee = tuitionFee,
                            applicationFee = applicationFee,
                            intake = intake,
                            description = description
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = addState !is Resource.Loading
            ) {
                if (addState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.height(22.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Add Program", fontSize = 16.sp)
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
