package com.piea.student.ui.screens.scholarships

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
class ScholarshipsViewModel @Inject constructor(
    private val repository: ScholarshipRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<List<Scholarship>>>(Resource.Loading)
    val state: StateFlow<Resource<List<Scholarship>>> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = Resource.Loading
            _state.value = repository.getScholarships()
        }
    }
}
