package com.piea.student.ui.screens.tracking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.piea.student.data.model.Application
import com.piea.student.ui.components.EmptyView
import com.piea.student.ui.components.ErrorView
import com.piea.student.ui.components.LoadingView
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Constants
import com.piea.student.utils.DateUtils
import com.piea.student.utils.Resource

@Composable
fun ApplicationTrackingScreen(
    viewModel: TrackingViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { PieaTopBar("Track Application", onBack) }) { padding ->
        when (val s = state) {
            is Resource.Loading -> LoadingView(Modifier.padding(padding))
            is Resource.Error -> ErrorView(s.message, Modifier.padding(padding))
            is Resource.Success -> {
                if (s.data.isEmpty()) {
                    EmptyView("You haven't submitted any application yet.", Modifier.padding(padding))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(s.data) { ApplicationCard(it) }
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun ApplicationCard(app: Application) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
                Text(app.preferredProgram.ifBlank { "Application" }, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
                StatusChip(app.status)
            }
            Text(
                "${app.preferredUniversity.ifBlank { "Any university" }} • ${app.preferredCountry}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                "Submitted: ${DateUtils.formatTimestamp(app.submittedAt)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
            if (app.remarks.isNotBlank()) {
                Text("Remarks: ${app.remarks}", fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val color = when (status) {
        Constants.STATUS_APPROVED -> Color(0xFF00B894)
        Constants.STATUS_REJECTED -> Color(0xFFD32F2F)
        Constants.STATUS_UNDER_REVIEW -> Color(0xFFFFA000)
        Constants.STATUS_DOCUMENTS_PENDING -> Color(0xFFFF7043)
        else -> Color(0xFF0B5FFF)
    }
    Surface(color = color.copy(alpha = 0.14f), shape = RoundedCornerShape(8.dp)) {
        Text(
            status,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
