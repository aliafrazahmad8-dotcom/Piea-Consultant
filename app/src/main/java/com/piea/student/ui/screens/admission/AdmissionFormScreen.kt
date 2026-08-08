package com.piea.student.ui.screens.admission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.data.model.Application
import com.piea.student.data.model.Program
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun AdmissionFormScreen(
    viewModel: AdmissionViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSubmitted: (applicationId: String, fee: String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var cnic by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var lastQualification by remember { mutableStateOf("") }
    var lastInstitution by remember { mutableStateOf("") }
    var marks by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }

    val programs by viewModel.programs.collectAsState()
    var selectedProgram by remember { mutableStateOf<Program?>(null) }
    var programDropdownExpanded by remember { mutableStateOf(false) }

    val submitState by viewModel.submitState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(submitState) {
        when (val state = submitState) {
            is Resource.Success -> {
                onSubmitted(state.data, selectedProgram?.applicationFee ?: "")
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
        topBar = { PieaTopBar("Online Admission Form", onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            SectionLabel("Personal Information")
            FormField("Full Name *", fullName) { fullName = it }
            FormField("Father's Name", fatherName) { fatherName = it }
            FormField("CNIC / Passport No. *", cnic) { cnic = it }
            FormField("Date of Birth (DD/MM/YYYY)", dob) { dob = it }
            FormField("Email *", email) { email = it }
            FormField("Phone Number *", phone) { phone = it }
            FormField("Address", address) { address = it }

            Divider(Modifier.padding(vertical = 16.dp))
            SectionLabel("Academic Background")
            FormField("Last Qualification", lastQualification) { lastQualification = it }
            FormField("Last Institution", lastInstitution) { lastInstitution = it }
            FormField("Marks / GPA", marks) { marks = it }

            Divider(Modifier.padding(vertical = 16.dp))
            SectionLabel("Study Preference")
            FormField("Preferred Country *", country) { country = it }
            FormField("Preferred University", university) { university = it }

            Text(
                "Preferred Program *",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            ExposedDropdownMenuBox(
                expanded = programDropdownExpanded,
                onExpandedChange = { programDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedProgram?.let { "${it.title} (${it.universityName})" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Select a program") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                ExposedDropdownMenu(
                    expanded = programDropdownExpanded,
                    onDismissRequest = { programDropdownExpanded = false }
                ) {
                    if (programs.isEmpty()) {
                        DropdownMenuItem(text = { Text("No programs available yet") }, onClick = {})
                    }
                    programs.forEach { program ->
                        DropdownMenuItem(
                            text = { Text("${program.title} — ${program.universityName}") },
                            onClick = {
                                selectedProgram = program
                                programDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            if (selectedProgram != null && selectedProgram!!.applicationFee.isNotBlank()) {
                Text(
                    "Application processing fee: Rs. ${selectedProgram!!.applicationFee}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
            } else {
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    viewModel.submit(
                        Application(
                            fullName = fullName, fatherName = fatherName, cnic = cnic,
                            dateOfBirth = dob, email = email, phoneNumber = phone, address = address,
                            lastQualification = lastQualification, lastInstitution = lastInstitution,
                            marksOrGpa = marks, preferredCountry = country,
                            preferredUniversity = university,
                            preferredProgram = selectedProgram?.title ?: "",
                            programId = selectedProgram?.id ?: "",
                            applicationFee = selectedProgram?.applicationFee ?: ""
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = submitState !is Resource.Loading
            ) {
                if (submitState is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.height(22.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Submit Application", fontSize = 16.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
private fun FormField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    )
}
