package com.piea.student.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piea.student.ui.components.PieaTopBar

@Composable
fun AdminPanelScreen(
    onBack: () -> Unit,
    onAddUniversity: () -> Unit,
    onAddScholarship: () -> Unit,
    onAddProgram: () -> Unit,
    onPublishUpdate: () -> Unit
) {
    Scaffold(topBar = { PieaTopBar("Admin Panel", onBack) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                "Add new content — it appears instantly for all students.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AdminRow(Icons.Default.School, "Add University", onAddUniversity)
            AdminRow(Icons.Default.CardMembership, "Add Scholarship", onAddScholarship)
            AdminRow(Icons.Default.MenuBook, "Add Program (with Fee)", onAddProgram)
            AdminRow(Icons.Default.SystemUpdate, "Publish App Update", onPublishUpdate)
        }
    }
}

@Composable
private fun AdminRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(label, modifier = Modifier.padding(start = 14.dp).weight(1f), fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}
