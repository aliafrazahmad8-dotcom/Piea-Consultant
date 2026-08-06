package com.piea.student.ui.screens.universities

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.ui.components.ErrorView
import com.piea.student.ui.components.LoadingView
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource

@Composable
fun UniversityDetailScreen(
    universityId: String,
    viewModel: UniversityDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(universityId) { viewModel.load(universityId) }
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { PieaTopBar("University Details", onBack) }) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingView(Modifier.padding(padding))
            is Resource.Error -> ErrorView(s.message, Modifier.padding(padding))
            is Resource.Success -> {
                val uni = s.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    Text(uni.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${uni.city}, ${uni.country}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Divider(Modifier.padding(vertical = 16.dp))

                    DetailRow("Ranking", uni.ranking)
                    DetailRow("Tuition Range", uni.tuitionRange)
                    DetailRow("Website", uni.website)

                    if (uni.description.isNotBlank()) {
                        Text(
                            "About",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                        )
                        Text(uni.description, fontSize = 14.sp)
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    if (value.isBlank()) return
    Column(Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
}
