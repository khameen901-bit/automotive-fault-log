package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostic_tests")
data class DiagnosticTest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val testName: String,
    val result: String,
    val notes: String? = null
)
