package com.khameen901.automotivefaultlog.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.khameen901.automotivefaultlog.data.local.entity.FaultRecordEntity
import com.khameen901.automotivefaultlog.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FaultRecordDao {
    @Insert
    suspend fun insertFaultRecord(faultRecord: FaultRecordEntity): Long

    @Update
    suspend fun updateFaultRecord(faultRecord: FaultRecordEntity): Int

    @Delete
    suspend fun deleteFaultRecord(faultRecord: FaultRecordEntity): Int

    @Query("DELETE FROM fault_records WHERE id = :faultId")
    suspend fun deleteFaultRecordById(faultId: Long): Int

    @Transaction
    @Query("SELECT * FROM fault_records ORDER BY recordingDate DESC")
    fun observeFaultRecordsWithVehicle(): Flow<List<FaultRecordWithVehicle>>

    @Transaction
    @Query("SELECT * FROM fault_records WHERE id = :faultId LIMIT 1")
    suspend fun getFaultRecordWithVehicleById(faultId: Long): FaultRecordWithVehicle?

    @Transaction
    @Query("SELECT * FROM fault_records ORDER BY recordingDate DESC")
    suspend fun getAllFaultRecordsWithVehicle(): List<FaultRecordWithVehicle>
}

data class FaultRecordWithVehicle(
    @Embedded val faultRecord: FaultRecordEntity,
    @Relation(
        parentColumn = "vehicleId",
        entityColumn = "id",
        entity = VehicleEntity::class
    )
    val vehicle: VehicleEntity?
)
