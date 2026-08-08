package com.piea.student.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Signup : Screen("signup")

    data object Dashboard : Screen("dashboard")
    data object Universities : Screen("universities")
    data object UniversityDetail : Screen("university_detail/{universityId}") {
        fun createRoute(universityId: String) = "university_detail/$universityId"
    }
    data object Scholarships : Screen("scholarships")
    data object Programs : Screen("programs")
    data object AdmissionForm : Screen("admission_form")
    data object FeePayment : Screen("fee_payment/{applicationId}/{feeAmount}") {
        fun createRoute(applicationId: String, feeAmount: String) =
            "fee_payment/$applicationId/${feeAmount.ifBlank { "0" }}"
    }
    data object DocumentUpload : Screen("document_upload")
    data object ApplicationTracking : Screen("application_tracking")
    data object Notifications : Screen("notifications")
    data object WhatsAppSupport : Screen("whatsapp_support")
    data object OfficeLocation : Screen("office_location")
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object AdminAddProgram : Screen("admin_add_program")
}
