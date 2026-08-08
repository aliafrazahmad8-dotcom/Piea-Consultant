package com.piea.student.utils

object Constants {
    const val COLLECTION_USERS = "users"
    const val COLLECTION_UNIVERSITIES = "universities"
    const val COLLECTION_SCHOLARSHIPS = "scholarships"
    const val COLLECTION_PROGRAMS = "programs"
    const val COLLECTION_APPLICATIONS = "applications"
    const val COLLECTION_NOTIFICATIONS = "notifications"
    const val COLLECTION_APP_CONFIG = "app_config"
    const val APP_VERSION_DOC_ID = "version"

    const val STORAGE_DOCUMENTS = "student_documents"
    const val STORAGE_PROFILE_PHOTOS = "profile_photos"

    const val PREFS_NAME = "piea_prefs"
    const val KEY_DARK_MODE = "dark_mode_enabled"

    const val STATUS_SUBMITTED = "Submitted"
    const val STATUS_FEE_PENDING = "Fee Payment Pending"
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

    const val WHATSAPP_NUMBER = "923154697566"

    // Fee payment details (manual transfer until a real payment gateway
    // merchant account with JazzCash/EasyPaisa is approved and integrated)
    const val JAZZCASH_NUMBER = "0309-0717979"
    const val BANK_NAME = "Meezan Bank"
    const val BANK_ACCOUNT_TITLE = "Ali Afraz Ahmad"
    const val BANK_ACCOUNT_NUMBER = "00300113304816"

    // Only this account (matched by login email) sees the in-app Admin panel.
    // Change this to your own PIEA Student login email.
    const val ADMIN_EMAIL = "aliafrazahmad8@gmail.com"
}
