package com.piea.student.data.model

data class University(
    val id: String = "",
    val name: String = "",
    val country: String = "",
    val city: String = "",
    val imageUrl: String = "",
    val ranking: String = "",
    val description: String = "",
    val tuitionRange: String = "",
    val website: String = "",
    val categories: String = "" // comma-separated, e.g. "MBBS, Bachelor, Master"
)
