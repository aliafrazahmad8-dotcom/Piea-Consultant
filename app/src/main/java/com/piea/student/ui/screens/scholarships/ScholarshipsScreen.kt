package com.piea.student.ui.screens.scholarships

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.data.model.Scholarship
import com.piea.student.ui.components.EmptyView
import com.piea.student.ui.components.ErrorView
import com.piea.student.ui.components.LoadingView
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.ui.theme.PieaGradients
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
        modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            // Colored accent header strip with icon badge and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PieaGradients.PrimaryHero)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.18f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.CardMembership, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        item.title,
                        color = androidx.compose.ui.graphics.Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "${item.provider}${if (item.country.isNotBlank()) " • ${item.country}" else ""}",
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }

            Column(Modifier.padding(16.dp)) {
                // Key stats row as chips (coverage % and deadline)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (item.coveragePercent.isNotBlank()) {
                        InfoChip(Icons.Default.Percent, "Coverage: ${item.coveragePercent}")
                    }
                    if (item.deadline.isNotBlank()) {
                        InfoChip(Icons.Default.EventBusy, "Deadline: ${item.deadline}")
                    }
                }

                if (item.eligibility.isNotBlank()) {
                    Text(
                        "Eligibility",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 14.dp, bottom = 2.dp)
                    )
                    Text(item.eligibility, fontSize = 13.sp)
                }

                if (item.description.isNotBlank()) {
                    Divider(Modifier.padding(vertical = 12.dp))
                    Text(item.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
            Text(
                label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}
