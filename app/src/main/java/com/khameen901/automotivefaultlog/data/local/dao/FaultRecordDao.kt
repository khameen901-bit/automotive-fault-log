package com.khameen901.automotivefaultlog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khameen901.automotivefaultlog.data.local.entity.VehicleEntity

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity): Long

    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity): Int

    @Query("SELECT * FROM vehicles WHERE id = :vehicleId LIMIT 1")
    suspend fun getVehicleById(vehicleId: Long): VehicleEntity?

    @Query("SELECT * FROM vehicles ORDER BY manufacturer, model, productionYear ASC")
    suspend fun getAllVehicles(): List<VehicleEntity>

    @Query("SELECT COUNT(*) FROM fault_records WHERE vehicleId = :vehicleId")
    suspend fun countFaultsForVehicle(vehicleId: Long): Int

    @Query("DELETE FROM vehicles WHERE id = :vehicleId")
    suspend fun deleteVehicle(vehicleId: Long): Int
}
