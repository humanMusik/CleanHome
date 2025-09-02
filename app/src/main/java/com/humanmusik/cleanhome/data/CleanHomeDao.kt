package com.humanmusik.cleanhome.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.data.entities.TaskLogEntity
import com.humanmusik.cleanhome.domain.model.ActionType
import com.humanmusik.cleanhome.domain.model.task.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface CleanHomeDao {

    //region Resident Daos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResident(residentEntity: ResidentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResidents(residentEntities: List<ResidentEntity>)

    @Query("DELETE FROM residententity")
    suspend fun deleteAllResidents()

    @Query("SELECT * from residententity")
    fun flowOfAllResidents(): Flow<List<ResidentEntity>>

    @Query("SELECT * FROM residententity WHERE id=:residentId")
    fun flowOfResidentById(residentId: Int): Flow<ResidentEntity>

    //endregion

    //region Room Daos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(roomEntity: RoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRooms(roomEntities: List<RoomEntity>)

    @Query("DELETE FROM roomentity")
    suspend fun deleteAllRooms()

    @Query("SELECT * FROM roomentity")
    fun flowOfAllRooms(): Flow<List<RoomEntity>>

    @Query("SELECT * FROM roomentity WHERE id=:roomId")
    fun flowOfRoomById(roomId: Int): Flow<RoomEntity>

    //endregion

    //region Task Daos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTasks(taskEntities: List<TaskEntity>)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query("DELETE FROM taskentity")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM taskentity")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM taskentity WHERE assigneeId=:residentId")
    suspend fun getTasksForResident(residentId: Int): List<TaskEntity>

    //endregion

    //region Enriched Task Daos

    @Query("SELECT * FROM enrichedtaskentity")
    fun flowOfEnrichedTasks(): Flow<List<EnrichedTaskEntity>>

    @Query("SELECT * FROM enrichedtaskentity WHERE id=:taskIdInt")
    fun flowOfEnrichedTaskById(taskIdInt: Int): Flow<EnrichedTaskEntity>
    //endregion

    //region TaskLog Daos

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskLog(taskLogEntity: TaskLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTaskLogs(taskLogEntities: List<TaskLogEntity>)

    @Query("DELETE FROM tasklogentity")
    suspend fun deleteAllTaskLogs()

    @Query("SELECT * from tasklogentity WHERE taskId=:taskId")
    fun getLogsByTaskId(taskId: Int): Flow<List<TaskLogEntity>>

    @Query("SELECT * from tasklogentity WHERE recordedAction=:actionType")
    fun getLogsByAction(actionType: ActionType): Flow<List<TaskLogEntity>>

    //endregion

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