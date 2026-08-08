package com.piea.student.ui.screens.scholarships

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.data.model.Scholarship
import com.piea.student.ui.components.EmptyView
import com.piea.student.ui.components.ErrorView
import com.piea.student.ui.components.LoadingView
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Resource

@Composable
fun ScholarshipsScreen(
    viewModel: ScholarshipsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { PieaTopBar("Scholarships", onBack) }) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingView(Modifier.padding(padding))
            is Resource.Error -> ErrorView(s.message, Modifier.padding(padding))
            is Resource.Success -> {
                if (s.data.isEmpty()) {
                    EmptyView("No scholarships posted right now.", Modifier.padding(padding))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(s.data) { ScholarshipCard(it) }
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun ScholarshipCard(item: Scholarship) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "${item.provider} • ${item.country}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (item.coveragePercent.isNotBlank()) {
                Text(
                    "Coverage: ${item.coveragePercent}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            if (item.deadline.isNotBlank()) {
                Text("Deadline: ${item.deadline}", fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
            }
            if (item.description.isNotBlank()) {
                Text(item.description, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
