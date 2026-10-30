package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fault_images")
data class FaultImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val imagePath: String,
    val createdAt: Long = System.currentTimeMillis()
)
