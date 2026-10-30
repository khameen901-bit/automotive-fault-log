package com.khameen901.automotivefaultlog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "diagnostic_tests",
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
data class DiagnosticTestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val name: String,
    val result: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
