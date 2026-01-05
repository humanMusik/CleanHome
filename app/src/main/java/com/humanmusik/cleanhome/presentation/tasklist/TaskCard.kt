package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.State
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.util.toDateString
import java.time.Duration
import java.time.LocalDate

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    enrichedTask: EnrichedTaskEntity,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            draggedElevation = 3.dp,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .heightIn(min = 60.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = enrichedTask.taskName,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Column(
                modifier = Modifier.padding(end = 10.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = enrichedTask.assigneeName,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = enrichedTask.roomName,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = enrichedTask.scheduledDate.toDateString("d MMM"),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview_TaskCard() {
    Box(
        modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)
    ) {
        TaskCard(
            enrichedTask = EnrichedTaskEntity(
                idValue = "task-123",
                taskName = "Vacuum",
                duration = Duration.ofMinutes(30),
                frequency = Frequency.Weekly,
                scheduledDate = LocalDate.now(),
                urgency = Urgency.Urgent,
                roomIdValue = "room-abc",
                roomName = "Living Room",
                assigneeIdValue = "user-xyz",
                assigneeName = "Leslie",
                state = State.Active,
                lastCompletedDate = null
            )
        ) { }
    }
}