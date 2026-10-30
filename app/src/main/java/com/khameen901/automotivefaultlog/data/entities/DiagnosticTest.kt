package com.khameen901.automotivefaultlog.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dtc_codes")
data class DtcCode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val faultRecordId: Long,
    val code: String
)
