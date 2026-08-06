package com.piea.student.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.model.User
import com.piea.student.data.repository.AuthRepository
import com.piea.student.data.repository.UserRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<Resource<User>>(Resource.Loading)
    val profileState: StateFlow<Resource<User>> = _profileState.asStateFlow()

    private val _updateState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val updateState: StateFlow<Resource<Unit>> = _updateState.asStateFlow()

    init { loadProfile() }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = Resource.Loading
            _profileState.value = userRepository.getCurrentUserProfile()
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            _updateState.value = userRepository.updateProfile(user)
            loadProfile()
        }
    }

    fun resetUpdateState() { _updateState.value = Resource.Idle }

    fun logout() = authRepository.logout()
}
