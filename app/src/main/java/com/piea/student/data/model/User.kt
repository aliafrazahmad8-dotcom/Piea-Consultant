package com.piea.student.data.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
    val cnic: String = "",
    val address: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
