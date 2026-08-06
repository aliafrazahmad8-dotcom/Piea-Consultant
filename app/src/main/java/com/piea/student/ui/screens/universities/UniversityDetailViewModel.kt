package com.piea.student.ui.screens.universities

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
class UniversityDetailViewModel @Inject constructor(
    private val repository: UniversityRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<University>>(Resource.Loading)
    val state: StateFlow<Resource<University>> = _state.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _state.value = Resource.Loading
            _state.value = repository.getUniversityById(id)
        }
    }
}
