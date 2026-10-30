package com.khameen901.automotivefaultlog.data.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.khameen901.automotivefaultlog.data.entities.DiagnosticTest
import com.khameen901.automotivefaultlog.data.entities.DtcCode
import com.khameen901.automotivefaultlog.data.entities.FaultImage
import com.khameen901.automotivefaultlog.data.entities.FaultRecord
import com.khameen901.automotivefaultlog.data.entities.VehicleInfo

data class FaultWithVehicle(
    @Embedded val fault: FaultRecord,
    @Relation(parentColumn = "vehicleId", entityColumn = "id")
    val vehicle: VehicleInfo?,
    @Relation(parentColumn = "id", entityColumn = "faultRecordId")
    val dtcCodes: List<DtcCode> = emptyList(),
    @Relation(parentColumn = "id", entityColumn = "faultRecordId")
    val tests: List<DiagnosticTest> = emptyList(),
    @Relation(parentColumn = "id", entityColumn = "faultRecordId")
    val images: List<FaultImage> = emptyList()
)
