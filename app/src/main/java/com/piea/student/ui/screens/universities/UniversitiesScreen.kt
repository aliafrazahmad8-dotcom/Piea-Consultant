package com.piea.student.ui.screens.universities

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.piea.student.data.model.University
import com.piea.student.ui.components.ErrorView
import com.piea.student.ui.components.LoadingView
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.ui.theme.PieaGradients
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
                UniversityDetailContent(s.data, Modifier.padding(padding))
            }
            else -> Unit
        }
    }
}

@Composable
private fun UniversityDetailContent(uni: University, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero photo (or gradient placeholder if no image was set)
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            if (uni.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = uni.imageUrl,
                    contentDescription = uni.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Subtle scrim so the name is legible if we overlay text later
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.15f))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PieaGradients.PrimaryHero),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(64.dp))
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(uni.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Text(
                    "${uni.city}${if (uni.city.isNotBlank() && uni.country.isNotBlank()) ", " else ""}${uni.country}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (uni.categories.isNotBlank()) {
                Row(modifier = Modifier.padding(top = 12.dp)) {
                    uni.categories.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { category ->
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                category,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Divider(Modifier.padding(vertical = 20.dp))

            // Key info cards
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                if (uni.ranking.isNotBlank()) {
                    InfoStatCard(Icons.Default.EmojiEvents, "Ranking", uni.ranking, Modifier.weight(1f))
                }
                if (uni.tuitionRange.isNotBlank()) {
                    InfoStatCard(Icons.Default.MonetizationOn, "Tuition", uni.tuitionRange, Modifier.weight(1f))
                }
            }

            if (uni.description.isNotBlank()) {
                Text(
                    "About",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
                Text(uni.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
            }

            if (uni.website.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .clickable {
                            val url = if (uni.website.startsWith("http")) uni.website else "https://${uni.website}"
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        },
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(
                            uni.website,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoStatCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
        }
    }
}
