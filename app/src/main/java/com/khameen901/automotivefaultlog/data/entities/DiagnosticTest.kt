package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "diagnostic_tests",
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
data class DiagnosticTest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long = 0L,
    val testName: String,
    val result: String,
    val notes: String? = null
)
