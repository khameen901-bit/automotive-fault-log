package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fault_records",
    foreignKeys = [
        ForeignKey(
            entity = VehicleInfo::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["vehicleId"])]
)
data class FaultRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val vehicleId: Long,
    val customerComplaint: String,
    val faultSymptoms: String,
    val relatedControlModule: String,
    val faultStatus: String,
    val freezeFrameData: String? = null,
    val diagnosticTestsPerformed: String? = null,
    val electricalMeasurements: String? = null,
    val testedComponents: String? = null,
    val suspectedCause: String? = null,
    val confirmedRootCause: String? = null,
    val repairProcedure: String? = null,
    val finalVerificationResult: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
