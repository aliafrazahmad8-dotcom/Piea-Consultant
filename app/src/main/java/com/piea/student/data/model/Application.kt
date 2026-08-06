package com.piea.student.data.model

data class Application(
    val id: String = "",
    val userId: String = "",
    val fullName: String = "",
    val fatherName: String = "",
    val cnic: String = "",
    val dateOfBirth: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val lastQualification: String = "",
    val lastInstitution: String = "",
    val marksOrGpa: String = "",
    val preferredCountry: String = "",
    val preferredUniversity: String = "",
    val preferredProgram: String = "",
    val status: String = "Submitted",
    val submittedAt: Long = System.currentTimeMillis(),
    val remarks: String = ""
)
