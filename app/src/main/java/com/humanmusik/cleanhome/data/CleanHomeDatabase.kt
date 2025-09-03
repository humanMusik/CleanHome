package com.humanmusik.cleanhome.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.data.entities.TaskLogEntity

const val DB_VERSION = 1

@Database(
    entities = [
        RoomEntity::class,
        ResidentEntity::class,
        TaskEntity::class,
        TaskLogEntity::class,
    ],
    views = [EnrichedTaskEntity::class],
    version = DB_VERSION,
)
abstract class CleanHomeDatabase : RoomDatabase() {

    abstract fun cleanHomeDao() : CleanHomeDao

    companion object {
        @Volatile
        private var INSTANCE: CleanHomeDatabase? = null

        fun getDbInstance(context: Context): CleanHomeDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CleanHomeDatabase::class.java, "clean_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}