package com.piea.student.utils

object Constants {
    const val COLLECTION_USERS = "users"
    const val COLLECTION_UNIVERSITIES = "universities"
    const val COLLECTION_SCHOLARSHIPS = "scholarships"
    const val COLLECTION_PROGRAMS = "programs"
    const val COLLECTION_APPLICATIONS = "applications"
    const val COLLECTION_NOTIFICATIONS = "notifications"

    const val STORAGE_DOCUMENTS = "student_documents"
    const val STORAGE_PROFILE_PHOTOS = "profile_photos"

    const val PREFS_NAME = "piea_prefs"
    const val KEY_DARK_MODE = "dark_mode_enabled"

    const val STATUS_SUBMITTED = "Submitted"
    const val STATUS_UNDER_REVIEW = "Under Review"
    const val STATUS_DOCUMENTS_PENDING = "Documents Pending"
    const val STATUS_APPROVED = "Approved"
    const val STATUS_REJECTED = "Rejected"

    val REQUIRED_DOCUMENTS = listOf(
        "Passport / CNIC",
        "Academic Transcript",
        "Degree Certificate",
        "IELTS / Language Certificate",
        "Passport Size Photo",
        "Statement of Purpose"
    )

    const val WHATSAPP_NUMBER = "923000000000"
}
