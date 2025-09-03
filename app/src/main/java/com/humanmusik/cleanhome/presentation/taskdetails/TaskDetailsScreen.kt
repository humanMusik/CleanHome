package com.humanmusik.cleanhome.presentation.taskdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.humanmusik.cleanhome.data.entities.EnrichedTaskEntity
import com.humanmusik.cleanhome.presentation.FlowState
import com.humanmusik.cleanhome.presentation.onSuccess
import com.humanmusik.cleanhome.util.toDateString
import java.time.format.FormatStyle

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState().value

    TaskDetailsContent(
        state = state,
    )
}

@Composable
private fun TaskDetailsContent(
    state: FlowState<EnrichedTaskEntity>,
) {
    state.onSuccess { enrichedTaskEntity ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = enrichedTaskEntity.taskName)
            Text(text = enrichedTaskEntity.roomName)
            Text(text = enrichedTaskEntity.scheduledDate.toDateString(FormatStyle.SHORT))
            Text(text = enrichedTaskEntity.assigneeName)
        }
    }
}