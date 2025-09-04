package com.humanmusik.cleanhome.presentation.taskdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cleanhome.R
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.toDateString
import java.time.format.FormatStyle
import kotlin.time.Duration

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel,
) {
    val state = viewModel.state.collectAsState().value

    TaskDetailsContent(state = state)
}

@Composable
private fun TaskDetailsContent(
    state: FlowState<EnrichedTaskEntity>,
) {
    state.onSuccess { enrichedTaskEntity ->
        Scaffold { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                EditCell(
                    cellTitle = "Task Name",
                    cellValue = enrichedTaskEntity.taskName,
                )

                EditCell(
                    cellTitle = "Room",
                    cellValue = enrichedTaskEntity.roomName,
                )

                Text(text = enrichedTaskEntity.scheduledDate.toDateString(FormatStyle.SHORT))
                Text(text = enrichedTaskEntity.frequency.toString())
                Text(text = enrichedTaskEntity.duration.toDurationString())
                Text(text = enrichedTaskEntity.urgency.toString())
                Text(text = enrichedTaskEntity.assigneeName)
            }
        }
    }
}

@Composable
private fun EditCell(
    modifier: Modifier = Modifier,
    cellTitle: String,
    cellValue: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .height(IntrinsicSize.Min)
            .clickable { /* TODO */ }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .fillMaxHeight()
                .align(CenterVertically),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = cellTitle,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = cellValue,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Icon(
            modifier = Modifier
                .align(CenterVertically)
                .size(64.dp)
                .padding(16.dp),
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit Task",
        )
    }
}

@Composable
private fun Duration.toDurationString(): String {
    val hours = inWholeHours
    val hoursSuffix = pluralStringResource(
        id = R.plurals.hours_suffix,
        count = hours.toInt(),
    )
    val minutes = inWholeMinutes
    val minutesSuffix = pluralStringResource(
        id = R.plurals.minutes_suffix,
        count = minutes.toInt(),
    )

    return buildString {
        append(hours)
        append(hoursSuffix)

        if (minutes > 0) {
            append(" and ")
            append(minutes)
            append(minutesSuffix)
        }
    }
}

@Preview
@Composable
fun Preview_EditCell() {
    Box {
        EditCell(
            cellTitle = "Task Name",
            cellValue = "Vacuum",
        )
    }
}