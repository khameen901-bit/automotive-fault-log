package com.khameen901.automotivefaultlog.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicles",
    indices = [Index(value = ["manufacturer", "model", "productionYear", "engineCode"]) ]
)
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
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
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
