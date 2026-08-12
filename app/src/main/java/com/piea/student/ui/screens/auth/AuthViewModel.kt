package com.piea.student.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.repository.AuthRepository
import com.piea.student.utils.PreferencesManager
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val loginState: StateFlow<Resource<Unit>> = _loginState.asStateFlow()

    private val _signupState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val signupState: StateFlow<Resource<Unit>> = _signupState.asStateFlow()

    private val _resetState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val resetState: StateFlow<Resource<Unit>> = _resetState.asStateFlow()

    private val _rememberedEmail = MutableStateFlow("")
    val rememberedEmail: StateFlow<String> = _rememberedEmail.asStateFlow()

    private val _rememberedPassword = MutableStateFlow("")
    val rememberedPassword: StateFlow<String> = _rememberedPassword.asStateFlow()

    private val _rememberMeChecked = MutableStateFlow(false)
    val rememberMeChecked: StateFlow<Boolean> = _rememberMeChecked.asStateFlow()

    private val _biometricEnabled = MutableStateFlow(false)
    val biometricEnabled: StateFlow<Boolean> = _biometricEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            val remembered = preferencesManager.isRememberMeEnabled()
            _rememberMeChecked.value = remembered
            if (remembered) {
                _rememberedEmail.value = preferencesManager.getRememberedEmail()
                _rememberedPassword.value = preferencesManager.getRememberedPassword()
            }
            _biometricEnabled.value = preferencesManager.isBiometricEnabled()
        }
    }

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun login(email: String, password: String, rememberMe: Boolean) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Please enter both email and password.")
            return
        }
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            val result = authRepository.login(email.trim(), password)
            if (result is Resource.Success) {
                if (rememberMe) {
                    preferencesManager.saveRememberedCredentials(email.trim(), password)
                } else {
                    preferencesManager.clearRememberedCredentials()
                }
            }
            _loginState.value = result
        }
    }

    /** Called after a successful biometric prompt — logs in with the saved credentials. */
    fun loginWithSavedCredentials() {
        val email = _rememberedEmail.value
        val password = _rememberedPassword.value
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("No saved credentials found. Please log in with your password first.")
            return
        }
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = authRepository.login(email, password)
        }
    }

    fun setBiometricEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesManager.setBiometricEnabled(enabled) }
    }

    fun signup(fullName: String, email: String, password: String, confirmPassword: String, phone: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || phone.isBlank()) {
            _signupState.value = Resource.Error("Please fill in all fields.")
            return
        }
        if (password.length < 6) {
            _signupState.value = Resource.Error("Password must be at least 6 characters.")
            return
        }
        if (password != confirmPassword) {
            _signupState.value = Resource.Error("Passwords do not match.")
            return
        }
        viewModelScope.launch {
            _signupState.value = Resource.Loading
            val result = authRepository.signup(fullName.trim(), email.trim(), password, phone.trim())
            if (result is Resource.Success) {
                authRepository.logout()
            }
            _signupState.value = result
        }
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _resetState.value = Resource.Error("Please enter your email first.")
            return
        }
        viewModelScope.launch {
            _resetState.value = Resource.Loading
            _resetState.value = authRepository.sendPasswordReset(email.trim())
        }
    }

    fun resetLoginState() { _loginState.value = Resource.Idle }
    fun resetSignupState() { _signupState.value = Resource.Idle }
}
