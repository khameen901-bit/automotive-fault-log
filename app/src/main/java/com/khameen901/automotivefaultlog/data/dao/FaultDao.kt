package com.khameen901.automotivefaultlog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khameen901.automotivefaultlog.data.entities.VehicleInfo

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleInfo): Long

    @Update
    suspend fun updateVehicle(vehicle: VehicleInfo)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: Long): VehicleInfo?

    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteVehicle(id: Long)
}
