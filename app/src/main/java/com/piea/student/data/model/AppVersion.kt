package com.piea.student.data.model

data class AppVersion(
    val latestVersionCode: Long = 0,
    val latestVersionName: String = "",
    val downloadUrl: String = "",
    val releaseNotes: String = ""
)
