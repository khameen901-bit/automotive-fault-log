package com.khameen901.automotivefaultlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khameen901.automotivefaultlog.data.local.entity.FaultImageEntity

@Dao
interface FaultImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaultImage(image: FaultImageEntity): Long

    @Delete
    suspend fun deleteFaultImage(image: FaultImageEntity): Int

    @Query("SELECT * FROM fault_images WHERE faultRecordId = :faultRecordId ORDER BY createdAt ASC")
    suspend fun getImagesForFault(faultRecordId: Long): List<FaultImageEntity>

    @Query("DELETE FROM fault_images WHERE faultRecordId = :faultRecordId")
    suspend fun deleteForFault(faultRecordId: Long): Int
}
