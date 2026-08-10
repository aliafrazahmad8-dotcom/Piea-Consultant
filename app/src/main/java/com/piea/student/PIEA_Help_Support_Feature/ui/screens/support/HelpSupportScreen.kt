package com.piea.student.ui.screens.support

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piea.student.ui.components.PieaTopBar
import com.piea.student.utils.Constants

private data class SupportOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBackground: Color
)

@Composable
fun HelpSupportScreen(
    onBack: () -> Unit,
    onFaq: () -> Unit,
    onFeedback: () -> Unit,
    onComplaint: () -> Unit,
    onWhatsApp: () -> Unit,
    onSocialMedia: () -> Unit
) {
    val context = LocalContext.current

    val options = listOf(
        SupportOption("FAQs", "Find answers to common questions", Icons.Default.HelpOutline, Color(0xFF1E63C4), Color(0xFFE3ECFB)) to onFaq,
        SupportOption("Feedback", "Share your thoughts with us", Icons.Default.Feedback, Color(0xFF7B3FA0), Color(0xFFEEE0F5)) to onFeedback,
        SupportOption("Complain", "Submit complaints easily for quick resolution", Icons.Default.ReportProblem, Color(0xFFD1462F), Color(0xFFFBE1DD)) to onComplaint,
        SupportOption("Call Us", "Reach us instantly for support", Icons.Default.Call, Color(0xFFD1462F), Color(0xFFFBE1DD)) to {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Constants.CONTACT_PHONE_NUMBER}"))
            context.startActivity(intent)
        },
        SupportOption("WhatsApp", "Chat with us on WhatsApp", Icons.Default.Chat, Color(0xFF2E8B57), Color(0xFFDFF3E6)) to onWhatsApp,
        SupportOption("Follow Us on Social Media", "Connect with us on social platforms", Icons.Default.Public, Color(0xFF1E63C4), Color(0xFFE3ECFB)) to onSocialMedia,
        SupportOption("Share App", "Spread the word! Share PIEA Student with your friends.", Icons.Default.Share, Color(0xFFCE8A1B), Color(0xFFFBEED2)) to {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check out the PIEA Student app for universities, scholarships, and admissions abroad!")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share PIEA Student"))
        }
    )

    Scaffold(topBar = { PieaTopBar("Help & Support", onBack) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "We're here to assist you! Choose an option below.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            options.forEach { (option, action) ->
                SupportRow(option, action)
            }
        }
    }
}

@Composable
private fun SupportRow(option: SupportOption, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(option.iconBackground),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(option.icon, contentDescription = null, tint = option.iconColor)
            }
            Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                Text(option.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    option.subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
