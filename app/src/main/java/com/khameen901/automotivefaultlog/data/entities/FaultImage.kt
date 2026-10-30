package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fault_images",
    foreignKeys = [
        ForeignKey(
            entity = FaultRecord::class,
            parentColumns = ["id"],
            childColumns = ["faultRecordId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["faultRecordId"]) ]
)
data class FaultImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val imagePath: String,
    val createdAt: Long = System.currentTimeMillis()
)
