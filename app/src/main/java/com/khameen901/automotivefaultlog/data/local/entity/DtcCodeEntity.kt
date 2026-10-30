package com.khameen901.automotivefaultlog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fault_records",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("vehicleId")]
)
data class FaultRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val vehicleId: Long,
    val customerComplaint: String = "",
    val symptoms: String = "",
    val controlModule: String = "",
    val faultStatus: String = "Current",
    val freezeFrame: String = "",
    val diagnosticTestsSummary: String = "",
    val electricalMeasurements: String = "",
    val testedComponents: String = "",
    val suspectedCause: String = "",
    val confirmedRootCause: String = "",
    val repairProcedure: String = "",
    val finalVerificationResult: String = "",
    val notes: String = "",
    val recordingDate: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
