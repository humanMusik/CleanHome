package com.humanmusik.cleanhome.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.humanmusik.cleanhome.data.entities.HomeEntity
import com.humanmusik.cleanhome.data.entities.HomeWithMetadata
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.ResidentRoomCrossRef
import com.humanmusik.cleanhome.data.entities.ResidentWithMetadata
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.RoomWithMetadata
import com.humanmusik.cleanhome.data.entities.TaskEntity

@Dao
interface CleanHomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(homeEntity: HomeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResident(residentEntity: ResidentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(roomEntity: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Upsert
    suspend fun upsertTask(taskEntity:TaskEntity)

    @Insert
    suspend fun insertResidentRoomCrossRef(residentRoomCrossRef: ResidentRoomCrossRef)

    @Query("SELECT * FROM taskentity")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM taskentity WHERE resident_id=:residentId")
    suspend fun getTasksForResident(residentId: Int): List<TaskEntity>

    @Query("SELECT * FROM homeentity WHERE id=:homeId")
    suspend fun getHomeWithMetadata(homeId: Int): HomeWithMetadata

    @Query("SELECT * from roomentity")
    suspend fun getAllRoomsWithMetadata(): List<RoomWithMetadata>

    @Query("SELECT * FROM roomentity WHERE id=:roomId")
    suspend fun getRoomWithMetadata(roomId: Int): RoomWithMetadata

    @Query("SELECT * from residententity")
    suspend fun getAllResidentsWithMetadata(): List<ResidentWithMetadata>

    @Query("SELECT * FROM roomentity WHERE id=:residentId")
    suspend fun getResidentWithMetadata(residentId: Int): ResidentWithMetadata
}