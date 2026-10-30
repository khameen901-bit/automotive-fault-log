package com.khameen901.automotivefaultlog.data.local.repository

import android.content.Context
import android.net.Uri
import com.khameen901.automotivefaultlog.data.local.dao.FaultRecordWithVehicle
import com.khameen901.automotivefaultlog.data.local.database.FaultDatabase
import com.khameen901.automotivefaultlog.data.local.entity.DiagnosticTestEntity
import com.khameen901.automotivefaultlog.data.local.entity.DtcCodeEntity
import com.khameen901.automotivefaultlog.data.local.entity.FaultImageEntity
import com.khameen901.automotivefaultlog.data.local.entity.FaultRecordEntity
import com.khameen901.automotivefaultlog.data.local.entity.VehicleEntity
import com.khameen901.automotivefaultlog.domain.model.FaultFilters
import com.khameen901.automotivefaultlog.domain.model.FaultFormData
import com.khameen901.automotivefaultlog.domain.model.FaultRecordDetail
import com.khameen901.automotivefaultlog.utils.BackupRestore
import com.khameen901.automotivefaultlog.utils.ImageStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.io.File

class FaultRepository(
    private val db: FaultDatabase,
    private val context: Context
) {
    private val vehicleDao = db.vehicleDao()
    private val faultRecordDao = db.faultRecordDao()
    private val dtcCodeDao = db.dtcCodeDao()
    private val diagnosticTestDao = db.diagnosticTestDao()
    private val faultImageDao = db.faultImageDao()

    fun observeFaultRecords(): Flow<List<FaultRecordWithVehicle>> = faultRecordDao.observeFaultRecordsWithVehicle()

    suspend fun getFaultRecords(): List<FaultRecordWithVehicle> = faultRecordDao.getAllFaultRecordsWithVehicle()

    suspend fun getFaultRecordById(faultId: Long): FaultRecordDetail? {
        val base = faultRecordDao.getFaultRecordWithVehicleById(faultId) ?: return null
        val dtcCodes = dtcCodeDao.getDtcCodesForFault(faultId)
        val diagnosticTests = diagnosticTestDao.getDiagnosticTestsForFault(faultId)
        val images = faultImageDao.getImagesForFault(faultId)
        return FaultRecordDetail(
            faultRecord = base.faultRecord,
            vehicle = base.vehicle,
            dtcCodes = dtcCodes,
            diagnosticTests = diagnosticTests,
            images = images
        )
    }

    suspend fun upsertFault(form: FaultFormData) {
        val now = System.currentTimeMillis()
        val vehicleItem = VehicleEntity(
            id = form.vehicleId,
            manufacturer = form.manufacturer.trim(),
            model = form.model.trim(),
            productionYear = form.productionYear,
            fuelType = form.fuelType.trim(),
            engineType = form.engineType.trim(),
            engineDisplacement = form.engineDisplacement.trim(),
            cylinderCount = form.cylinderCount.trim(),
            engineCode = form.engineCode.trim(),
            transmissionType = form.transmissionType.trim(),
            drivetrain = form.drivetrain.trim(),
            vin = form.vin.trim(),
            updatedAt = now
        )

        val vehicleId = if (form.vehicleId > 0L) {
            vehicleDao.updateVehicle(vehicleItem)
            form.vehicleId
        } else {
            vehicleDao.insertVehicle(vehicleItem)
        }

        val faultRecord = FaultRecordEntity(
            id = form.id,
            vehicleId = vehicleId,
            customerComplaint = form.customerComplaint.trim(),
            symptoms = form.symptoms.trim(),
            controlModule = form.controlModule.trim(),
            faultStatus = form.faultStatus.ifBlank { "Current" },
            freezeFrame = form.freezeFrame.trim(),
            diagnosticTestsSummary = form.diagnosticTests.joinToString(", "),
            electricalMeasurements = form.electricalMeasurements.trim(),
            testedComponents = form.testedComponents.trim(),
            suspectedCause = form.suspectedCause.trim(),
            confirmedRootCause = form.confirmedRootCause.trim(),
            repairProcedure = form.repairProcedure.trim(),
            finalVerificationResult = form.finalVerificationResult.trim(),
            notes = form.notes.trim(),
            recordingDate = form.recordingDate,
            updatedAt = now,
            createdAt = if (form.id > 0L) {
                faultRecordDao.getFaultRecordWithVehicleById(form.id)?.faultRecord?.createdAt ?: now
            } else {
                now
            }
        )

        val faultId = if (form.id > 0L) {
            val existing = faultRecordDao.getFaultRecordWithVehicleById(form.id)
            if (existing == null) {
                faultRecordDao.insertFaultRecord(faultRecord)
            } else {
                faultRecordDao.updateFaultRecord(faultRecord)
                form.id
            }
        } else {
            faultRecordDao.insertFaultRecord(faultRecord)
        }

        if (form.id > 0L) {
            dtcCodeDao.deleteForFault(form.id)
            diagnosticTestDao.deleteForFault(form.id)
            faultImageDao.deleteForFault(form.id)
        }

        val dtcs = form.dtcCodes.filter { it.isNotBlank() }.map {
            DtcCodeEntity(faultRecordId = faultId, code = it.trim(), description = "")
        }
        dtcs.forEach { dtcCodeDao.insertDtcCode(it) }

        val tests = form.diagnosticTests.filter { it.isNotBlank() }.map {
            DiagnosticTestEntity(faultRecordId = faultId, name = it.trim(), result = "OK", notes = "")
        }
        tests.forEach { diagnosticTestDao.insertDiagnosticTest(it) }

        val imagePaths = form.imagePaths.filter { it.isNotBlank() }
        imagePaths.forEach { imagePath ->
            val file = File(imagePath)
            if (file.exists()) {
                val fileName = file.nameWithoutExtension
                faultImageDao.insertFaultImage(
                    FaultImageEntity(
                        faultRecordId = faultId,
                        filePath = file.absolutePath,
                        fileName = fileName
                    )
                )
            }
        }
    }

    suspend fun deleteFault(faultId: Long) {
        val existing = faultRecordDao.getFaultRecordWithVehicleById(faultId) ?: return
        val imageFiles = faultImageDao.getImagesForFault(faultId).mapNotNull { File(it.filePath) }
        imageFiles.forEach { if (it.exists()) it.delete() }
        faultRecordDao.deleteFaultRecordById(faultId)
        val vehicleId = existing.faultRecord.vehicleId
        val remaining = faultRecordDao.getAllFaultRecordsWithVehicle().count { it.faultRecord.vehicleId == vehicleId }
        if (remaining == 0) {
            vehicleDao.deleteVehicle(vehicleId)
        }
    }

    suspend fun searchFaults(query: String, filters: FaultFilters): List<FaultRecordWithVehicle> {
        val allRecords = getFaultRecords()
        val normalizedQuery = query.trim()

        return allRecords.filter { record ->
            val vehicle = record.vehicle
            val fault = record.faultRecord
            val dtcs = dtcCodeDao.getDtcCodesForFault(record.faultRecord.id)
            val matchesQuery = normalizedQuery.isBlank() ||
                fault.customerComplaint.contains(normalizedQuery, ignoreCase = true) ||
                fault.symptoms.contains(normalizedQuery, ignoreCase = true) ||
                fault.controlModule.contains(normalizedQuery, ignoreCase = true) ||
                vehicle?.manufacturer.orEmpty().contains(normalizedQuery, ignoreCase = true) ||
                vehicle?.model.orEmpty().contains(normalizedQuery, ignoreCase = true) ||
                vehicle?.engineCode.orEmpty().contains(normalizedQuery, ignoreCase = true) ||
                dtcs.any { it.code.contains(normalizedQuery, ignoreCase = true) }

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

    suspend fun backupDatabase(outputUri: Uri): Boolean = BackupRestore.backupDatabase(context, outputUri)

    suspend fun restoreDatabase(inputUri: Uri): Boolean = BackupRestore.restoreDatabase(context, inputUri)
}
