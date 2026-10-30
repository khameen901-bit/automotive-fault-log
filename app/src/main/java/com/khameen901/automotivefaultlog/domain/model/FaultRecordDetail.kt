package com.khameen901.automotivefaultlog.domain.model

data class FaultFormData(
    val id: Long = 0L,
    val vehicleId: Long = 0L,
    val manufacturer: String = "",
    val model: String = "",
    val productionYear: Int = 0,
    val fuelType: String = "",
    val engineType: String = "",
    val engineDisplacement: String = "",
    val cylinderCount: String = "",
    val engineCode: String = "",
    val transmissionType: String = "",
    val drivetrain: String = "",
    val vin: String = "",
    val customerComplaint: String = "",
    val symptoms: String = "",
    val dtcCodes: List<String> = emptyList(),
    val controlModule: String = "",
    val faultStatus: String = "Current",
    val freezeFrame: String = "",
    val diagnosticTests: List<String> = emptyList(),
    val electricalMeasurements: String = "",
    val testedComponents: String = "",
    val suspectedCause: String = "",
    val confirmedRootCause: String = "",
    val repairProcedure: String = "",
    val finalVerificationResult: String = "",
    val notes: String = "",
    val recordingDate: Long = System.currentTimeMillis(),
    val imagePaths: List<String> = emptyList()
)

data class FaultFilters(
    val manufacturer: String = "",
    val model: String = "",
    val productionYear: String = "",
    val engineType: String = "",
    val engineDisplacement: String = "",
    val engineCode: String = "",
    val dtcCode: String = "",
    val controlModule: String = "",
    val symptoms: String = ""
)
