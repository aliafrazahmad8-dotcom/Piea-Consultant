package com.piea.student.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.repository.AuthRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val loginState: StateFlow<Resource<Unit>> = _loginState.asStateFlow()

    private val _signupState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val signupState: StateFlow<Resource<Unit>> = _signupState.asStateFlow()

    private val _resetState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val resetState: StateFlow<Resource<Unit>> = _resetState.asStateFlow()

    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Please enter both email and password.")
            return
        }
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = authRepository.login(email.trim(), password)
        }
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
