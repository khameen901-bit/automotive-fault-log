package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicles",
    indices = [Index(value = ["vin"], unique = true)]
)
data class VehicleInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val manufacturer: String,
    val model: String,
    val productionYear: Int,
    val fuelType: String,
    val engineType: String,
    val engineDisplacement: String,
    val numberOfCylinders: Int,
    val engineCode: String,
    val transmissionType: String,
    val drivetrain: String,
    val vin: String? = null
)
