package com.piea.student.ui.screens.admission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.model.Application
import com.piea.student.data.repository.ApplicationRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdmissionViewModel @Inject constructor(
    private val repository: ApplicationRepository
) : ViewModel() {

    private val _submitState = MutableStateFlow<Resource<String>>(Resource.Idle)
    val submitState: StateFlow<Resource<String>> = _submitState.asStateFlow()

    fun submit(application: Application) {
        if (application.fullName.isBlank() || application.cnic.isBlank() ||
            application.email.isBlank() || application.phoneNumber.isBlank() ||
            application.preferredCountry.isBlank() || application.preferredProgram.isBlank()
        ) {
            _submitState.value = Resource.Error("Please fill in all required fields.")
            return
        }
        viewModelScope.launch {
            _submitState.value = Resource.Loading
            _submitState.value = repository.submitApplication(application)
        }
    }

    fun resetState() { _submitState.value = Resource.Idle }
}
