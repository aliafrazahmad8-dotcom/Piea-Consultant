package com.piea.student.ui.screens.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.repository.SupportRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportFormViewModel @Inject constructor(
    private val repository: SupportRepository
) : ViewModel() {

    private val _submitState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val submitState: StateFlow<Resource<Unit>> = _submitState.asStateFlow()

    fun submit(type: String, subject: String, message: String) {
        if (message.isBlank()) {
            _submitState.value = Resource.Error("Please enter your message.")
            return
        }
        viewModelScope.launch {
            _submitState.value = Resource.Loading
            _submitState.value = repository.submitMessage(type, subject, message)
        }
    }

    fun resetState() { _submitState.value = Resource.Idle }
}
