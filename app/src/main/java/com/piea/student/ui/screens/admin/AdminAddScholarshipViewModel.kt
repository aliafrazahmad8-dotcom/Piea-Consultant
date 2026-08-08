package com.piea.student.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.model.Scholarship
import com.piea.student.data.repository.ScholarshipRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddScholarshipViewModel @Inject constructor(
    private val repository: ScholarshipRepository
) : ViewModel() {

    private val _addState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val addState: StateFlow<Resource<Unit>> = _addState.asStateFlow()

    fun addScholarship(scholarship: Scholarship) {
        if (scholarship.title.isBlank() || scholarship.provider.isBlank()) {
            _addState.value = Resource.Error("Please fill in at least Title and Provider.")
            return
        }
        viewModelScope.launch {
            _addState.value = Resource.Loading
            _addState.value = repository.addScholarship(scholarship)
        }
    }

    fun resetState() { _addState.value = Resource.Idle }
}
