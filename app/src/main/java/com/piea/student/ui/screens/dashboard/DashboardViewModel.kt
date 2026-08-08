package com.piea.student.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.BuildConfig
import com.piea.student.data.model.AppVersion
import com.piea.student.data.model.User
import com.piea.student.data.repository.AppUpdateRepository
import com.piea.student.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.piea.student.utils.Resource

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appUpdateRepository: AppUpdateRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _availableUpdate = MutableStateFlow<AppVersion?>(null)
    val availableUpdate: StateFlow<AppVersion?> = _availableUpdate.asStateFlow()

    init {
        loadUser()
        checkForUpdate()
    }

    private fun loadUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUserProfile()) {
                is Resource.Success -> _user.value = result.data
                else -> Unit
            }
        }
    }

    private fun checkForUpdate() {
        viewModelScope.launch {
            val result = appUpdateRepository.getLatestVersion()
            if (result is Resource.Success) {
                val latest = result.data
                if (latest.latestVersionCode > BuildConfig.VERSION_CODE && latest.downloadUrl.isNotBlank()) {
                    _availableUpdate.value = latest
                }
            }
        }
    }

    fun dismissUpdateDialog() {
        _availableUpdate.value = null
    }
}
