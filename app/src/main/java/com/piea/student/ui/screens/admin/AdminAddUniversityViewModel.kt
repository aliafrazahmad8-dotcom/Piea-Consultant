package com.piea.student.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.model.University
import com.piea.student.data.repository.UniversityRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAddUniversityViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private val _addState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val addState: StateFlow<Resource<Unit>> = _addState.asStateFlow()

    fun addUniversity(university: University) {
        if (university.name.isBlank() || university.country.isBlank()) {
            _addState.value = Resource.Error("Please fill in at least Name and Country.")
            return
        }
        viewModelScope.launch {
            _addState.value = Resource.Loading
            _addState.value = repository.addUniversity(university)
        }
    }

    fun resetState() { _addState.value = Resource.Idle }
}
