package com.piea.student.ui.screens.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piea.student.data.model.DocumentItem
import com.piea.student.data.repository.DocumentRepository
import com.piea.student.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val repository: DocumentRepository
) : ViewModel() {

    private val _documents = MutableStateFlow<Resource<List<DocumentItem>>>(Resource.Loading)
    val documents: StateFlow<Resource<List<DocumentItem>>> = _documents.asStateFlow()

    private val _uploadState = MutableStateFlow<Resource<DocumentItem>>(Resource.Idle)
    val uploadState: StateFlow<Resource<DocumentItem>> = _uploadState.asStateFlow()

    init { loadDocuments() }

    fun loadDocuments() {
        viewModelScope.launch {
            _documents.value = Resource.Loading
            _documents.value = repository.getMyDocuments()
        }
    }

    fun uploadDocument(documentType: String, fileName: String, bytes: ByteArray) {
        viewModelScope.launch {
            _uploadState.value = Resource.Loading
            val result = repository.uploadDocument(documentType, fileName, bytes)
            _uploadState.value = result
            if (result is Resource.Success) loadDocuments()
        }
    }

    fun resetUploadState() { _uploadState.value = Resource.Idle }
}
