package com.piea.student.data.model

data class Program(
    val id: String = "",
    val title: String = "",
    val universityName: String = "",
    val degreeLevel: String = "", // Bachelor, Master, PhD, Diploma
    val duration: String = "",
    val tuitionFee: String = "",
    val intake: String = "",
    val description: String = ""
)
