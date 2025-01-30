package com.example.cleanhome.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.cleanhome.data.entities.House
import com.example.cleanhome.data.entities.Resident
import com.example.cleanhome.data.entities.Room
import com.example.cleanhome.data.entities.Task
import com.example.cleanhome.data.entities.relations.HouseWithResidents
import com.example.cleanhome.data.entities.relations.ResidentRoomCrossRef
import com.example.cleanhome.data.entities.relations.ResidentWithRooms
import com.example.cleanhome.data.entities.relations.ResidentWithTasks
import com.example.cleanhome.data.entities.relations.RoomWithResidents
import com.example.cleanhome.data.entities.relations.RoomWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface CleanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(house: House)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResident(resident: Resident)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: Room)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Upsert
    suspend fun upsertTask(task:Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResidentRoomCrossRef(crossRef: ResidentRoomCrossRef)

    @Transaction
    @Query("SELECT * FROM house WHERE houseName=:houseName")
    fun getHouseWithResidents(houseName: String): Flow<List<HouseWithResidents>>

    @Transaction
    @Query("SELECT * FROM resident WHERE residentId=:residentId")
    fun getResidentWithTasks(residentId: Int): Flow<List<ResidentWithTasks>>

    @Transaction
    @Query("SELECT * FROM room WHERE roomId=:roomId")
    fun getRoomWithTasks(roomId: Int): Flow<List<RoomWithTasks>>

    @Transaction
    @Query("SELECT * FROM room WHERE roomId=:roomId")
    fun getResidentsOfRoom(roomId: Int): Flow<List<RoomWithResidents>>

    @Transaction
    @Query("SELECT * FROM resident WHERE residentId=:residentId")
    fun getRoomsOfResident(residentId: Int): Flow<List<ResidentWithRooms>>

    @Transaction
    @Query("SELECT * FROM task WHERE assignedTo=:residentId")
    fun getTasksOfResident(residentId: Int): Flow<List<Task>>
}