package com.khameen901.automotivefaultlog.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.khameen901.automotivefaultlog.data.dao.FaultDao
import com.khameen901.automotivefaultlog.data.dao.VehicleDao
import com.khameen901.automotivefaultlog.data.entities.DiagnosticTest
import com.khameen901.automotivefaultlog.data.entities.DtcCode
import com.khameen901.automotivefaultlog.data.entities.FaultImage
import com.khameen901.automotivefaultlog.data.entities.FaultRecord
import com.khameen901.automotivefaultlog.data.entities.VehicleInfo

@Database(
    entities = [
        VehicleInfo::class,
        FaultRecord::class,
        DtcCode::class,
        DiagnosticTest::class,
        FaultImage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FaultDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun faultDao(): FaultDao

    companion object {
        @Volatile
        private var INSTANCE: FaultDatabase? = null

        fun getInstance(context: Context): FaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaultDatabase::class.java,
                    "automotive_fault_log.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
