package com.piea.student.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.repository.ApplicationRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeePaymentViewModel @Inject constructor(
    private val repository: ApplicationRepository
) : ViewModel() {

    private val _submitState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val submitState: StateFlow<Resource<Unit>> = _submitState.asStateFlow()

    fun submitReceipt(applicationId: String, fileName: String, bytes: ByteArray) {
        viewModelScope.launch {
            _submitState.value = Resource.Loading
            _submitState.value = repository.submitFeeReceipt(applicationId, fileName, bytes)
        }
    }

    fun resetState() { _submitState.value = Resource.Idle }
}
