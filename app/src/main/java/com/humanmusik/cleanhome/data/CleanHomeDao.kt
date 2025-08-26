package com.humanmusik.cleanhome.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.humanmusik.cleanhome.data.entities.HomeEntity
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.ResidentRoomCrossRef
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.RoomWithMetadata
import com.humanmusik.cleanhome.data.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CleanHomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(homeEntity: HomeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResident(residentEntity: ResidentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResidents(residentEntities: List<ResidentEntity>)

    @Query("DELETE FROM residententity")
    suspend fun deleteAllResidents()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(roomEntity: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRooms(roomEntities: List<RoomEntity>)

    @Query("DELETE FROM roomentity")
    suspend fun deleteAllRooms()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(taskEntities: List<TaskEntity>)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Insert
    suspend fun insertResidentRoomCrossRef(residentRoomCrossRef: ResidentRoomCrossRef)

    @Query("DELETE FROM taskentity")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM taskentity")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM taskentity WHERE resident_id=:residentId")
    suspend fun getTasksForResident(residentId: Int): List<TaskEntity>

    //    @Query("SELECT * FROM homeentity WHERE id=:homeId")
//    suspend fun getHomeWithMetadata(homeId: Int): HomeWithMetadata
//
    @Query("SELECT * from roomentity")
    fun getAllRooms(): Flow<List<RoomEntity>>
//
//    @Query("SELECT * FROM roomentity WHERE id=:roomId")
//    suspend fun getRoomWithMetadata(roomId: Int): RoomWithMetadata
//
    @Query("SELECT * from residententity")
    suspend fun getAllResidentsWithMetadata(): List<ResidentEntity>
//
//    @Query("SELECT * FROM roomentity WHERE id=:residentId")
//    suspend fun getResidentWithMetadata(residentId: Int): ResidentWithMetadata

    @Transaction
    suspend fun deleteAndInsertTasks(tasks: List<TaskEntity>) {
        deleteAllTasks()
        insertAllTasks(tasks)
    }

    @Transaction
    suspend fun deleteAndInsertRooms(rooms: List<RoomEntity>) {
        deleteAllRooms()
        insertAllRooms(rooms)
    }

    @Transaction
    suspend fun deleteAndInsertResidents(residents: List<ResidentEntity>) {
        deleteAllResidents()
        insertAllResidents(residents)
    }
}