package com.khameen901.automotivefaultlog.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dtc_codes",
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
data class DtcCodeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val code: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
