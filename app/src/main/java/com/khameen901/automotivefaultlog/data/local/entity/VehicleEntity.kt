package com.khameen901.automotivefaultlog.domain.model

import com.khameen901.automotivefaultlog.data.local.entity.DiagnosticTestEntity
import com.khameen901.automotivefaultlog.data.local.entity.DtcCodeEntity
import com.khameen901.automotivefaultlog.data.local.entity.FaultImageEntity
import com.khameen901.automotivefaultlog.data.local.entity.FaultRecordEntity
import com.khameen901.automotivefaultlog.data.local.entity.VehicleEntity

data class FaultRecordDetail(
    val faultRecord: FaultRecordEntity,
    val vehicle: VehicleEntity?,
    val dtcCodes: List<DtcCodeEntity>,
    val diagnosticTests: List<DiagnosticTestEntity>,
    val images: List<FaultImageEntity>
)
