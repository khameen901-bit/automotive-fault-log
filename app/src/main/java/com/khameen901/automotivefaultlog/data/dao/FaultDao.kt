package com.khameen901.automotivefaultlog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.khameen901.automotivefaultlog.data.entities.DiagnosticTest
import com.khameen901.automotivefaultlog.data.entities.DtcCode
import com.khameen901.automotivefaultlog.data.entities.FaultImage
import com.khameen901.automotivefaultlog.data.entities.FaultRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FaultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaultRecord(record: FaultRecord): Long

    @Update
    suspend fun updateFaultRecord(record: FaultRecord)

    @Transaction
    @Query("SELECT * FROM fault_records ORDER BY createdAt DESC")
    fun getFaultsWithDetails(): Flow<List<FaultWithVehicle>>

    @Query("SELECT * FROM fault_records WHERE id = :id")
    suspend fun getFaultById(id: Long): FaultRecord?

    @Query("SELECT COUNT(*) FROM fault_records WHERE vehicleId = :vehicleId")
    suspend fun countFaultsForVehicle(vehicleId: Long): Int

    @Query("DELETE FROM fault_records WHERE id = :id")
    suspend fun deleteFaultRecord(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDtc(dtc: DtcCode): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiagnosticTest(test: DiagnosticTest): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: FaultImage): Long

    @Query("SELECT * FROM dtc_codes WHERE faultRecordId = :faultId")
    suspend fun getDtcCodesForFault(faultId: Long): List<DtcCode>

    @Query("SELECT * FROM diagnostic_tests WHERE faultRecordId = :faultId")
    suspend fun getDiagnosticTestsForFault(faultId: Long): List<DiagnosticTest>

    @Query("SELECT * FROM fault_images WHERE faultRecordId = :faultId")
    suspend fun getImagesForFault(faultId: Long): List<FaultImage>

    @Query("DELETE FROM dtc_codes WHERE faultRecordId = :faultId")
    suspend fun deleteDtcCodesForFault(faultId: Long)

    @Query("DELETE FROM diagnostic_tests WHERE faultRecordId = :faultId")
    suspend fun deleteDiagnosticTestsForFault(faultId: Long)

    @Query("DELETE FROM fault_images WHERE faultRecordId = :faultId")
    suspend fun deleteImagesForFault(faultId: Long)
}
