package com.piea.student.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.piea.student.ui.components.PieaBottomBar
import com.piea.student.ui.screens.admin.AdminAddProgramScreen
import com.piea.student.ui.screens.admission.AdmissionFormScreen
import com.piea.student.ui.screens.auth.LoginScreen
import com.piea.student.ui.screens.auth.SignupScreen
import com.piea.student.ui.screens.dashboard.DashboardScreen
import com.piea.student.ui.screens.documents.DocumentUploadScreen
import com.piea.student.ui.screens.map.OfficeLocationScreen
import com.piea.student.ui.screens.notifications.NotificationsScreen
import com.piea.student.ui.screens.payment.FeePaymentScreen
import com.piea.student.ui.screens.programs.ProgramsScreen
import com.piea.student.ui.screens.profile.ProfileScreen
import com.piea.student.ui.screens.scholarships.ScholarshipsScreen
import com.piea.student.ui.screens.settings.SettingsScreen
import com.piea.student.ui.screens.splash.SplashScreen
import com.piea.student.ui.screens.support.WhatsAppSupportScreen
import com.piea.student.ui.screens.tracking.ApplicationTrackingScreen
import com.piea.student.ui.screens.universities.UniversitiesScreen
import com.piea.student.ui.screens.universities.UniversityDetailScreen

/** Routes that show the bottom navigation bar. */
private val bottomBarRoutes = setOf(
    Screen.Dashboard.route,
    Screen.Universities.route,
    Screen.Programs.route,
    Screen.Notifications.route,
    Screen.Profile.route
)

@Composable
fun PieaNavGraph(isLoggedIn: Boolean) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                PieaBottomBar(navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = androidx.compose.ui.Modifier.padding(
                bottom = if (currentRoute in bottomBarRoutes) padding.calculateBottomPadding() else 0.dp
            )
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    isLoggedIn = isLoggedIn,
                    onFinished = { loggedIn ->
                        val destination = if (loggedIn) Screen.Dashboard.route else Screen.Login.route
                        navController.navigate(destination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
                )
            }

            composable(Screen.Signup.route) {
                SignupScreen(
                    onSignupSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(onActionClick = { route -> navController.navigate(route) })
            }

            composable(Screen.Universities.route) {
                UniversitiesScreen(
                    onBack = { navController.popBackStack() },
                    onUniversityClick = { id -> navController.navigate(Screen.UniversityDetail.createRoute(id)) }
                )
            }

            composable(
                route = Screen.UniversityDetail.route,
                arguments = listOf(navArgument("universityId") { })
            ) { backStackEntry ->
                val universityId = backStackEntry.arguments?.getString("universityId") ?: ""
                UniversityDetailScreen(universityId = universityId, onBack = { navController.popBackStack() })
            }

            composable(Screen.Scholarships.route) {
                ScholarshipsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Programs.route) {
                ProgramsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.AdmissionForm.route) {
                AdmissionFormScreen(
                    onBack = { navController.popBackStack() },
                    onSubmitted = { applicationId, fee ->
                        navController.navigate(Screen.FeePayment.createRoute(applicationId, fee)) {
                            popUpTo(Screen.AdmissionForm.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Screen.FeePayment.route,
                arguments = listOf(navArgument("applicationId") { }, navArgument("feeAmount") { })
            ) { backStackEntry ->
                val applicationId = backStackEntry.arguments?.getString("applicationId") ?: ""
                val feeAmount = backStackEntry.arguments?.getString("feeAmount") ?: "0"
                FeePaymentScreen(
                    applicationId = applicationId,
                    feeAmount = feeAmount,
                    onDone = {
                        navController.navigate(Screen.ApplicationTracking.route) {
                            popUpTo(Screen.Dashboard.route)
                        }
                    }
                )
            }

            composable(Screen.DocumentUpload.route) {
                DocumentUploadScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.ApplicationTracking.route) {
                ApplicationTrackingScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Notifications.route) {
                NotificationsScreen()
            }

            composable(Screen.WhatsAppSupport.route) {
                WhatsAppSupportScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.OfficeLocation.route) {
                OfficeLocationScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onOpenSettings = { navController.navigate(Screen.Settings.route) },
                    onOpenAdmin = { navController.navigate(Screen.AdminAddProgram.route) },
                    onLoggedOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.AdminAddProgram.route) {
                AdminAddProgramScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
