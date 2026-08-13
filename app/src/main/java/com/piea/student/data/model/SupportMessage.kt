package com.piea.student.data.model

data class SupportMessage(
    val id: String = "",
    val userId: String = "",
    val type: String = "", // "Feedback" or "Complaint"
    val subject: String = "",
    val message: String = "",
    val submittedAt: Long = System.currentTimeMillis()
)
