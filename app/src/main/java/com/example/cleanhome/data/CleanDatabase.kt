package com.example.cleanhome.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cleanhome.data.entities.House
import com.example.cleanhome.data.entities.Resident
import com.example.cleanhome.data.entities.Room
import com.example.cleanhome.data.entities.Task
import com.example.cleanhome.data.entities.relations.ResidentRoomCrossRef

@Database(
    entities = [
        House::class,
        Room::class,
        Resident::class,
        Task::class,
        ResidentRoomCrossRef::class,
    ],
    version = 1,
)
abstract class CleanDatabase : RoomDatabase() {

    abstract val dao: CleanDao

    companion object {
        @Volatile
        private var INSTANCE: CleanDatabase? = null

        fun getInstance(context: Context): CleanDatabase {
            synchronized(this) {
                return INSTANCE ?: androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    CleanDatabase::class.java,
                    "clean_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}