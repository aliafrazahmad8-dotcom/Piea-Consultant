package com.piea.student.ui.screens.support

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piea.student.ui.components.PieaTopBar

private data class FaqItem(val question: String, val answer: String)

private val faqs = listOf(
    FaqItem(
        "How do I apply for admission?",
        "Go to the Admission Form from the Dashboard, select your preferred program, fill in your details, and submit. You'll then be guided through the application fee payment."
    ),
    FaqItem(
        "How do I pay the application fee?",
        "After submitting the Admission Form, you'll see the fee screen with JazzCash and Bank Transfer details. Pay via either method, then upload a screenshot of your receipt for verification."
    ),
    FaqItem(
        "How can I track my application status?",
        "Open 'Track Application' from the Dashboard to see the live status of your submitted applications, from Submitted to Approved."
    ),
    FaqItem(
        "What documents do I need to upload?",
        "Typically your Passport/CNIC, Academic Transcript, Degree Certificate, Language Certificate, Photo, and Statement of Purpose. Check the Upload Documents screen for the full list."
    ),
    FaqItem(
        "How do I contact PIEA for help?",
        "Use the Help & Support section for WhatsApp, Call, Feedback, or Complaint options — we're happy to assist."
    )
)

@Composable
fun FAQScreen(onBack: () -> Unit) {
    Scaffold(topBar = { PieaTopBar("FAQs", onBack) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(faqs) { faq -> FaqCard(faq) }
        }
    }
}

@Composable
private fun FaqCard(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    faq.question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (expanded) {
                Text(
                    faq.answer,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}
