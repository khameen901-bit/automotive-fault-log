package com.khameen901.automotivefaultlog.data.repository

import android.content.Context
import android.net.Uri
import com.khameen901.automotivefaultlog.data.dao.FaultWithVehicle
import com.khameen901.automotivefaultlog.data.database.FaultDatabase
import com.khameen901.automotivefaultlog.data.entities.DiagnosticTest
import com.khameen901.automotivefaultlog.data.entities.DtcCode
import com.khameen901.automotivefaultlog.data.entities.FaultImage
import com.khameen901.automotivefaultlog.data.entities.FaultRecord
import com.khameen901.automotivefaultlog.data.entities.VehicleInfo
import com.khameen901.automotivefaultlog.utils.copyUriToAppStorage
import com.khameen901.automotivefaultlog.utils.deleteFileIfExists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FaultRepository(private val context: Context) {
    private val database = FaultDatabase.getInstance(context)
    val allFaults: Flow<List<FaultWithVehicle>> = database.faultDao().getFaultsWithDetails()

    suspend fun upsertFault(
        vehicle: VehicleInfo,
        faultRecord: FaultRecord,
        dtcCodes: List<String>,
        diagnosticTests: List<DiagnosticTest>,
        imagePaths: List<String>
    ) = withContext(Dispatchers.IO) {
        database.runInTransaction {
            val vehicleId = if (vehicle.id > 0L) {
                database.vehicleDao().updateVehicle(vehicle)
                vehicle.id
            } else {
                database.vehicleDao().insertVehicle(vehicle)
            }

            val faultId = if (faultRecord.id > 0L) {
                database.faultDao().updateFaultRecord(faultRecord.copy(vehicleId = vehicleId))
                faultRecord.id
            } else {
                database.faultDao().insertFaultRecord(faultRecord.copy(vehicleId = vehicleId))
            }

            database.faultDao().deleteDtcCodesForFault(faultId)
            database.faultDao().deleteDiagnosticTestsForFault(faultId)
            database.faultDao().deleteImagesForFault(faultId)

            dtcCodes.filter { it.isNotBlank() }.forEach { code ->
                database.faultDao().insertDtc(DtcCode(faultRecordId = faultId, code = code.trim()))
            }

            diagnosticTests.filter { it.testName.isNotBlank() }.forEach { test ->
                database.faultDao().insertDiagnosticTest(test.copy(faultRecordId = faultId))
            }

            imagePaths.filter { it.isNotBlank() }.forEach { path ->
                database.faultDao().insertImage(FaultImage(faultRecordId = faultId, imagePath = path))
            }
        }
    }

    suspend fun deleteFault(faultId: Long) = withContext(Dispatchers.IO) {
        database.runInTransaction {
            val record = database.faultDao().getFaultById(faultId) ?: return@runInTransaction

            database.faultDao().getImagesForFault(faultId).forEach { image ->
                deleteFileIfExists(File(image.imagePath))
            }

            database.faultDao().deleteDtcCodesForFault(faultId)
            database.faultDao().deleteDiagnosticTestsForFault(faultId)
            database.faultDao().deleteImagesForFault(faultId)
            database.faultDao().deleteFaultRecord(faultId)

            val faultsStillUsingVehicle = database.faultDao().countFaultsForVehicle(record.vehicleId) > 0
            if (!faultsStillUsingVehicle) {
                database.vehicleDao().deleteVehicle(record.vehicleId)
            }
        }
    }

    suspend fun savePhotoFromUri(uri: Uri): String? = withContext(Dispatchers.IO) {
        copyUriToAppStorage(context, uri)
    }

    suspend fun backupDatabaseTo(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val dbFile = context.getDatabasePath("automotive_fault_log.db")
            if (!dbFile.exists()) return@withContext false
            context.contentResolver.openOutputStream(uri)?.use { output ->
                FileInputStream(dbFile).use { input ->
                    input.copyTo(output)
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun restoreDatabaseFrom(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val dbFile = context.getDatabasePath("automotive_fault_log.db")
            database.close()
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(dbFile).use { output ->
                    input.copyTo(output)
                }
            }
            FaultDatabase.resetInstance()
            true
        } catch (_: Exception) {
            false
        }
    }
}
