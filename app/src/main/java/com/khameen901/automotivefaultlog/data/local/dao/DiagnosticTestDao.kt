package com.khameen901.automotivefaultlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khameen901.automotivefaultlog.data.local.entity.DtcCodeEntity

@Dao
interface DtcCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDtcCode(dtcCode: DtcCodeEntity): Long

    @Delete
    suspend fun deleteDtcCode(dtcCode: DtcCodeEntity): Int

    @Query("SELECT * FROM dtc_codes WHERE faultRecordId = :faultRecordId ORDER BY code ASC")
    suspend fun getDtcCodesForFault(faultRecordId: Long): List<DtcCodeEntity>

    @Query("DELETE FROM dtc_codes WHERE faultRecordId = :faultRecordId")
    suspend fun deleteForFault(faultRecordId: Long): Int
}
