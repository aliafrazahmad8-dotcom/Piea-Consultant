package com.piea.student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.piea.student.navigation.PieaNavGraph
import com.piea.student.ui.theme.PieaStudentTheme
import com.piea.student.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var preferencesManager: PreferencesManager
    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val systemDark = isSystemInDarkTheme()
            val darkModeEnabled by preferencesManager.isDarkModeEnabled.collectAsState(initial = systemDark)

            PieaStudentTheme(darkTheme = darkModeEnabled) {
                Surface(modifier = Modifier) {
                    PieaNavGraph(isLoggedIn = firebaseAuth.currentUser != null)
                }
            }
        }
    }
}
