package com.khameen901.automotivefaultlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khameen901.automotivefaultlog.data.local.entity.DiagnosticTestEntity

@Dao
interface DiagnosticTestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosticTest(test: DiagnosticTestEntity): Long

    @Delete
    suspend fun deleteDiagnosticTest(test: DiagnosticTestEntity): Int

    @Query("SELECT * FROM diagnostic_tests WHERE faultRecordId = :faultRecordId ORDER BY createdAt ASC")
    suspend fun getDiagnosticTestsForFault(faultRecordId: Long): List<DiagnosticTestEntity>

    @Query("DELETE FROM diagnostic_tests WHERE faultRecordId = :faultRecordId")
    suspend fun deleteForFault(faultRecordId: Long): Int
}
