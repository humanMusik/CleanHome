package com.humanmusik.cleanhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.entities.ResidentEntity
import com.humanmusik.cleanhome.data.entities.RoomEntity
import com.humanmusik.cleanhome.data.entities.TaskEntity
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.navigation.NavigationRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationRoot(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
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
                assignedTo = ResidentEntity(
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
                assignedTo = ResidentEntity(
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
                assignedTo = ResidentEntity(
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
                assignedTo = ResidentEntity(
                    id = 2,
                    name = "Jenny",
                    homeId = 1,
                ),
            ),
        )

        val tasks = listOf(
            Task(
                id = 1,
                name = "Vacuum",
                room = Room(
                    id = 3,
                    name = "Cihan bedroom",
                    homeId = 1,
                ),
                duration = 10.minutes,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                assigneeId = Resident(
                    id = 3,
                    name = "Cihan",
                    homeId = 1,
                ),
            ),
            Task(
                id = 2,
                name = "Clean toilet",
                room = Room(
                    id = 2,
                    name = "Bathroom",
                    homeId = 1,
                ),
                duration = 15.minutes,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                assigneeId = Resident(
                    id = 2,
                    name = "Jenny",
                    homeId = 1,
                ),
            ),
            Task(
                id = 3,
                name = "Tidy",
                room = Room(
                    id = 4,
                    name = "Living Room",
                    homeId = 1,
                ),
                duration = 1.hours,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                assigneeId = Resident(
                    id = 1,
                    name = "Leslie",
                    homeId = 1,
                ),
            ),
            Task(
                id = 4,
                name = "Mop",
                room = Room(
                    id = 2,
                    name = "Bathroom",
                ),
                duration = 5.minutes,
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.NonUrgent,
                assigneeId = Resident(
                    id = 2,
                    name = "Jenny",
                ),
            ),
        )

        lifecycleScope.launch(Dispatchers.IO) {
//            homeEntity.forEach { dao.insertHouse(it) }
            dao.deleteAndInsertResidents(residentEntities)
            dao.deleteAndInsertRooms(roomEntities)
            dao.deleteAndInsertTasks(taskEntities)
//            taskEntities.forEach { dao.insertTask(it) }
//            residentRoomRelations.forEach { dao.insertResidentRoomCrossRef(it) }

            val getAllTasks = dao.getAllTasks()
//            val getHomeWithMetadata = dao.getHomeWithMetadata(1)
//            val getAllRoomsWithMetadata = dao.getAllRoomsWithMetadata()
//            val getAllResidentsWithMetadata = dao.getAllResidentsWithMetadata()
            val getTasksForLeslie = dao.getTasksForResident(1)

//            getAllRoomsWithMetadata.onEach(::println)
//            getAllResidentsWithMetadata.onEach(::println)
//            println(getHomeWithMetadata)
            getTasksForLeslie.onEach(::println)
        }
    }
}
