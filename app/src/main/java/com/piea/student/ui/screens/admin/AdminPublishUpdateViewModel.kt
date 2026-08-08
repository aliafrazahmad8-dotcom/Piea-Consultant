package com.piea.student.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.model.AppVersion
import com.piea.student.data.repository.AppUpdateRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPublishUpdateViewModel @Inject constructor(
    private val repository: AppUpdateRepository
) : ViewModel() {

    private val _publishState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val publishState: StateFlow<Resource<Unit>> = _publishState.asStateFlow()

    fun publishUpdate(versionCode: String, versionName: String, downloadUrl: String, releaseNotes: String) {
        val code = versionCode.toLongOrNull()
        if (code == null || versionName.isBlank() || downloadUrl.isBlank()) {
            _publishState.value = Resource.Error("Please enter a valid version code, version name, and download link.")
            return
        }
        viewModelScope.launch {
            _publishState.value = Resource.Loading
            _publishState.value = repository.publishUpdate(
                AppVersion(
                    latestVersionCode = code,
                    latestVersionName = versionName,
                    downloadUrl = downloadUrl,
                    releaseNotes = releaseNotes
                )
            )
        }
    }

    fun resetState() { _publishState.value = Resource.Idle }
}
