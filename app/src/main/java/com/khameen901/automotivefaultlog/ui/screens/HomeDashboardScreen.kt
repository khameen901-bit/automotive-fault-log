package com.khameen901.automotivefaultlog.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.khameen901.automotivefaultlog.data.dao.FaultWithVehicle
import com.khameen901.automotivefaultlog.data.entities.DiagnosticTest
import com.khameen901.automotivefaultlog.data.entities.FaultRecord
import com.khameen901.automotivefaultlog.data.entities.VehicleInfo
import com.khameen901.automotivefaultlog.data.repository.FaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FaultViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaultRepository(application)

    private val _records = MutableStateFlow<List<FaultWithVehicle>>(emptyList())
    val records: StateFlow<List<FaultWithVehicle>> = _records.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allFaults.collectLatest { _records.value = it }
        }
    }

    fun saveFault(
        vehicle: VehicleInfo,
        faultRecord: FaultRecord,
        dtcCodes: List<String>,
        diagnosticTests: List<DiagnosticTest>,
        imagePaths: List<String>
    ) = viewModelScope.launch {
        repository.upsertFault(vehicle, faultRecord, dtcCodes, diagnosticTests, imagePaths)
    }

    fun deleteFault(faultId: Long) = viewModelScope.launch {
        repository.deleteFault(faultId)
    }

    suspend fun savePhotoFromUri(uri: Uri): String? = repository.savePhotoFromUri(uri)

    suspend fun backupDatabase(targetPath: String): Boolean = repository.backupDatabaseTo(targetPath)

    suspend fun restoreDatabase(uri: Uri): Boolean = repository.restoreDatabaseFrom(uri)
}
