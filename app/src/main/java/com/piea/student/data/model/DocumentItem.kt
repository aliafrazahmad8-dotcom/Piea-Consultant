package com.piea.student.data.model

data class DocumentItem(
    val id: String = "",
    val userId: String = "",
    val documentType: String = "",
    val fileName: String = "",
    val fileUrl: String = "",
    val uploadedAt: Long = System.currentTimeMillis(),
    val verified: Boolean = false
)
