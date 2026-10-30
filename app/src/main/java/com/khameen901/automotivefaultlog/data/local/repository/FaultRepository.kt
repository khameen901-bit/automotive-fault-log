package com.khameen901.automotivefaultlog.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.khameen901.automotivefaultlog.data.local.dao.DiagnosticTestDao
import com.khameen901.automotivefaultlog.data.local.dao.DtcCodeDao
import com.khameen901.automotivefaultlog.data.local.dao.FaultImageDao
import com.khameen901.automotivefaultlog.data.local.dao.FaultRecordDao
import com.khameen901.automotivefaultlog.data.local.dao.VehicleDao
import com.khameen901.automotivefaultlog.data.local.entity.DiagnosticTestEntity
import com.khameen901.automotivefaultlog.data.local.entity.DtcCodeEntity
import com.khameen901.automotivefaultlog.data.local.entity.FaultImageEntity
import com.khameen901.automotivefaultlog.data.local.entity.FaultRecordEntity
import com.khameen901.automotivefaultlog.data.local.entity.VehicleEntity

@Database(
    entities = [
        VehicleEntity::class,
        FaultRecordEntity::class,
        DtcCodeEntity::class,
        DiagnosticTestEntity::class,
        FaultImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FaultDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun faultRecordDao(): FaultRecordDao
    abstract fun dtcCodeDao(): DtcCodeDao
    abstract fun diagnosticTestDao(): DiagnosticTestDao
    abstract fun faultImageDao(): FaultImageDao

    companion object {
        const val DB_NAME = "automotive_fault_log.db"

        @Volatile
        private var INSTANCE: FaultDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Reserved for future schema changes.
            }
        }

        fun getInstance(context: Context): FaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaultDatabase::class.java,
                    DB_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun resetInstance() {
            INSTANCE = null
        }
    }
}
