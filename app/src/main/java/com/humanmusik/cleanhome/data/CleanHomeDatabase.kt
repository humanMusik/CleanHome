package com.humanmusik.cleanhome.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.humanmusik.cleanhome.data.entities.HomeEntity
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.ResidentRoomCrossRef
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.TaskEntity

@Database(
    entities = [
        HomeEntity::class,
        RoomEntity::class,
        ResidentEntity::class,
        TaskEntity::class,
        ResidentRoomCrossRef::class,
    ],
    version = 1,
)
abstract class CleanHomeDatabase : RoomDatabase() {

    abstract val dao: CleanHomeDao

    companion object {
        @Volatile
        private var INSTANCE: CleanHomeDatabase? = null

        fun getInstance(context: Context): CleanHomeDatabase {
            synchronized(this) {
                return INSTANCE ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    CleanHomeDatabase::class.java,
                    "clean_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}