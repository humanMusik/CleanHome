package com.humanmusik.cleanhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.entities.HomeEntity
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.ResidentRoomCrossRef
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.presentation.tasklist.TaskListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                TaskListScreen()
            }
        }

        val dao = CleanHomeDatabase.getDbInstance(this).cleanHomeDao()

        val homeEntity = listOf(HomeEntity(name = "221b Baker St"))

        val residentEntities = listOf(
            ResidentEntity(
                name = "Leslie",
                homeId = 1,
            ),
            ResidentEntity(
                name = "Jenny",
                homeId = 1,
            ),
            ResidentEntity(
                name = "Cihan",
                homeId = 1,
            ),
        )

        val roomEntities = listOf(
            RoomEntity(
                name = "JL bedroom",
                homeId = 1,
            ),
            RoomEntity(
                name = "Bathroom",
                homeId = 1,
            ),
            RoomEntity(
                name = "Cihan bedroom",
                homeId = 1,
            ),
            RoomEntity(
                name = "Living Room",
                homeId = 1,
            ),
        )

        val taskEntities = listOf(
            TaskEntity(
                name = "Vacuum",
                room = RoomEntity(
                    id = 3,
                    name = "Cihan bedroom",
                    homeId = 1,
                ),
                duration = 10.minutes,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                allocatedTo = ResidentEntity(
                    id = 3,
                    name = "Cihan",
                    homeId = 1,
                ),
            ),
            TaskEntity(
                name = "Clean toilet",
                room = RoomEntity(
                    id = 2,
                    name = "Bathroom",
                    homeId = 1,
                ),
                duration = 15.minutes,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                allocatedTo = ResidentEntity(
                    id = 2,
                    name = "Jenny",
                    homeId = 1,
                ),
            ),
            TaskEntity(
                name = "Tidy",
                room = RoomEntity(
                    id = 4,
                    name = "Living Room",
                    homeId = 1,
                ),
                duration = 1.hours,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                allocatedTo = ResidentEntity(
                    id = 1,
                    name = "Leslie",
                    homeId = 1,
                ),
            ),
            TaskEntity(
                name = "Mop",
                room = RoomEntity(
                    id = 2,
                    name = "Bathroom",
                    homeId = 1,
                ),
                duration = 5.minutes,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                allocatedTo = ResidentEntity(
                    id = 2,
                    name = "Jenny",
                    homeId = 1,
                ),
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
//            homeEntity.forEach { dao.insertHouse(it) }
//            residentEntities.forEach { dao.insertResident(it) }
//            roomEntities.forEach { dao.insertRoom(it) }
//            taskEntities.forEach { dao.insertTask(it) }
//            residentRoomRelations.forEach { dao.insertResidentRoomCrossRef(it) }

            val getAllTasks = dao.getAllTasks()
//            val getHomeWithMetadata = dao.getHomeWithMetadata(1)
//            val getAllRoomsWithMetadata = dao.getAllRoomsWithMetadata()
//            val getAllResidentsWithMetadata = dao.getAllResidentsWithMetadata()
            val getTasksForLeslie = dao.getTasksForResident(1)

            getAllTasks.onEach(::println)
//            getAllRoomsWithMetadata.onEach(::println)
//            getAllResidentsWithMetadata.onEach(::println)
//            println(getHomeWithMetadata)
            getTasksForLeslie.onEach(::println)
        }
    }
}
