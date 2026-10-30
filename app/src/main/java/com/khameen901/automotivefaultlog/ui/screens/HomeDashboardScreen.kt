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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FaultViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaultRepository(application)

    val records: StateFlow<List<FaultWithVehicle>> = repository.allFaults
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    suspend fun backupDatabase(uri: Uri): Boolean = repository.backupDatabaseTo(uri)

    suspend fun restoreDatabase(uri: Uri): Boolean = repository.restoreDatabaseFrom(uri)
}
