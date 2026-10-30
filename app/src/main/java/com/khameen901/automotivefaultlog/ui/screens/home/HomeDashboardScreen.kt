package com.khameen901.automotivefaultlog.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.khameen901.automotivefaultlog.data.local.dao.FaultRecordWithVehicle
import com.khameen901.automotivefaultlog.data.local.database.FaultDatabase
import com.khameen901.automotivefaultlog.data.local.repository.FaultRepository
import com.khameen901.automotivefaultlog.domain.model.FaultFilters
import com.khameen901.automotivefaultlog.domain.model.FaultFormData
import com.khameen901.automotivefaultlog.domain.model.FaultRecordDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FaultListUiState(
    val records: List<FaultRecordWithVehicle> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FaultViewModel(
    private val repository: FaultRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FaultListUiState())
    val state: StateFlow<FaultListUiState> = _state

    private val _selectedFault = MutableStateFlow<FaultRecordDetail?>(null)
    val selectedFault: StateFlow<FaultRecordDetail?> = _selectedFault

    fun loadFaults() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val records = repository.getFaultRecords()
                _state.value = FaultListUiState(records = records, isLoading = false)
            } catch (e: Exception) {
                _state.value = FaultListUiState(isLoading = false, error = e.message ?: "Failed to load records")
            }
        }
    }

    fun searchRecords(query: String, filters: FaultFilters): List<FaultRecordWithVehicle> {
        return _state.value.records.filter { record ->
            val vehicle = record.vehicle
            val fault = record.faultRecord
            val dtcs = repository.getFaultRecordById(record.faultRecord.id)?.dtcCodes ?: emptyList()
            val matchesQuery = query.isBlank() ||
                fault.customerComplaint.contains(query, ignoreCase = true) ||
                fault.symptoms.contains(query, ignoreCase = true) ||
                fault.controlModule.contains(query, ignoreCase = true) ||
                vehicle?.manufacturer.orEmpty().contains(query, ignoreCase = true) ||
                vehicle?.model.orEmpty().contains(query, ignoreCase = true) ||
                vehicle?.engineCode.orEmpty().contains(query, ignoreCase = true) ||
                dtcs.any { it.code.contains(query, ignoreCase = true) }

            val matchesFilters = (filters.manufacturer.isBlank() || vehicle?.manufacturer?.contains(filters.manufacturer, ignoreCase = true) == true) &&
                (filters.model.isBlank() || vehicle?.model?.contains(filters.model, ignoreCase = true) == true) &&
                (filters.productionYear.isBlank() || vehicle?.productionYear.toString() == filters.productionYear) &&
                (filters.engineType.isBlank() || vehicle?.engineType?.contains(filters.engineType, ignoreCase = true) == true) &&
                (filters.engineDisplacement.isBlank() || vehicle?.engineDisplacement?.contains(filters.engineDisplacement, ignoreCase = true) == true) &&
                (filters.engineCode.isBlank() || vehicle?.engineCode?.contains(filters.engineCode, ignoreCase = true) == true) &&
                (filters.dtcCode.isBlank() || dtcs.any { it.code.contains(filters.dtcCode, ignoreCase = true) }) &&
                (filters.controlModule.isBlank() || fault.controlModule.contains(filters.controlModule, ignoreCase = true)) &&
                (filters.symptoms.isBlank() || fault.symptoms.contains(filters.symptoms, ignoreCase = true))

            matchesQuery && matchesFilters
        }
    }

    fun getFaultById(faultId: Long) {
        viewModelScope.launch {
            _selectedFault.value = repository.getFaultRecordById(faultId)
        }
    }

    fun saveFault(form: FaultFormData) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                repository.upsertFault(form)
                loadFaults()
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message ?: "Failed to save record")
            }
        }
    }

    fun deleteFault(faultId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteFault(faultId)
                loadFaults()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to delete record")
            }
        }
    }

    suspend fun backupDatabase(context: Context, outputUri: Uri): Boolean {
        return repository.backupDatabase(outputUri)
    }

    suspend fun restoreDatabase(context: Context, inputUri: Uri): Boolean {
        val result = repository.restoreDatabase(inputUri)
        if (result) {
            loadFaults()
        }
        return result
    }
}

class FaultViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = FaultDatabase.getInstance(application)
        val repository = FaultRepository(database, application)
        @Suppress("UNCHECKED_CAST")
        return FaultViewModel(repository) as T
    }
}
