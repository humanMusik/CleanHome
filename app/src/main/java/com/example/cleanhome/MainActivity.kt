package com.example.cleanhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.cleanhome.data.CleanDatabase
import com.example.cleanhome.data.entities.Frequency
import com.example.cleanhome.data.entities.House
import com.example.cleanhome.data.entities.Resident
import com.example.cleanhome.data.entities.Room
import com.example.cleanhome.data.entities.Task
import com.example.cleanhome.data.entities.Urgency
import com.example.cleanhome.data.entities.relations.ResidentRoomCrossRef
import com.example.cleanhome.ui.theme.CleanHomeTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = CleanDatabase.getInstance(this).dao

        val house = listOf(House(houseName = "221b Baker St"))

        val residents = listOf(
            Resident(
                residentName = "Leslie",
                houseId = 1,
            ),
            Resident(
                residentName = "Jenny",
                houseId = 1,
            ),
            Resident(
                residentName = "Cihan",
                houseId = 1,
            ),
        )

        val rooms = listOf(
            Room(
                roomName = "JL bedroom",
                houseId = 1,
            ),
            Room(
                roomName = "Bathroom",
                houseId = 1,
            ),
            Room(
                roomName = "Cihan bedroom",
                houseId = 1,
            ),
            Room(
                roomName = "Living Room",
                houseId = 1,
            ),
        )

        val tasks = listOf(
            Task(
                taskName = "Vacuum",
                roomId = 3,
                duration = 1000L,
                frequency = Frequency.Weekly,
                scheduledDate = OffsetDateTime.now(),
                urgency = Urgency.NonUrgent,
                assignedTo = 2,
            ),
            Task(
                taskName = "Clean toilet",
                roomId = 2,
                duration = 1000L,
                frequency = Frequency.Weekly,
                scheduledDate = OffsetDateTime.now(),
                urgency = Urgency.NonUrgent,
                assignedTo = 1,
            ),
            Task(
                taskName = "Tidy",
                roomId = 3,
                duration = 1000L,
                frequency = Frequency.Weekly,
                scheduledDate = OffsetDateTime.now(),
                urgency = Urgency.NonUrgent,
                assignedTo = 3,
            ),
            Task(
                taskName = "Vacuum",
                roomId = 1,
                duration = 1000L,
                frequency = Frequency.Weekly,
                scheduledDate = OffsetDateTime.now(),
                urgency = Urgency.NonUrgent,
                assignedTo = 1,
            ),
        )

        val residentRoomRelations = listOf(
            ResidentRoomCrossRef(1, 1),
            ResidentRoomCrossRef(2, 1),
            ResidentRoomCrossRef(3, 3),
            ResidentRoomCrossRef(1, 2),
            ResidentRoomCrossRef(2, 2),
            ResidentRoomCrossRef(3, 2),
            ResidentRoomCrossRef(1, 4),
            ResidentRoomCrossRef(2, 4),
            ResidentRoomCrossRef(3, 4),
        )

        lifecycleScope.launch {
            house.forEach { dao.insertHouse(it) }
            residents.forEach { dao.insertResident(it) }
            rooms.forEach { dao.insertRoom(it) }
            tasks.forEach { dao.insertTask(it) }
            residentRoomRelations.forEach { dao.insertResidentRoomCrossRef(it) }

            val residentsOfJLBedroom = dao.getResidentsOfRoom(1)
            val lesliesRooms = dao.getRoomsOfResident(1)
            val lesliesTasks = dao.getResidentWithTasks(1)
            val livingRoomTasks = dao.getRoomWithTasks(3)
            val leslieTasks = dao.getTasksOfResident(1)

//            residentsOfJLBedroom.collect {
//                println("Resident of JL Bedroom: ${it.first().residents}")
//            }

//            lesliesRooms.collect {
//                println("Leslies Rooms: ${it.first().rooms}")
//            }

//            lesliesTasks.collect {
//                println("Leslies tasks: ${it.first().tasks}")
//            }

//            livingRoomTasks.collect {
//                println("CBedroom tasks: ${it.first().tasks.size}")
//            }

            leslieTasks.collect {
                println("leslie tasks: ${it.size}")
            }
        }
    }
}
