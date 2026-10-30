package com.khameen901.automotivefaultlog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fault_images",
    foreignKeys = [
        ForeignKey(
            entity = FaultRecordEntity::class,
            parentColumns = ["id"],
            childColumns = ["faultRecordId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("faultRecordId")]
)
data class FaultImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val filePath: String,
    val fileName: String,
    val createdAt: Long = System.currentTimeMillis()
)
