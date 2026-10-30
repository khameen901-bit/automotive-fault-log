package com.khameen901.automotivefaultlog.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
    exportSchema = false
)
abstract class FaultDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun faultDao(): FaultDao

    companion object {
        @Volatile
        private var INSTANCE: FaultDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE INDEX IF NOT EXISTS index_fault_records_vehicleId ON fault_records(vehicleId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_dtc_codes_faultRecordId ON dtc_codes(faultRecordId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_tests_faultRecordId ON diagnostic_tests(faultRecordId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_fault_images_faultRecordId ON fault_images(faultRecordId)")
            }
        }

        fun getInstance(context: Context): FaultDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FaultDatabase::class.java,
                    "automotive_fault_log.db"
                )
                    .addMigrations(MIGRATION_1_2)
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
