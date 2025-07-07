package com.humanmusik.cleanhome.presentation.tasklist

import android.widget.Toast
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.humanmusik.cleanhome.domain.model.task.Task

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onEdit: (Task) -> Unit,
    onComplete: (Task) -> Unit,
) {
    val context = LocalContext.current
    val currentItem by rememberUpdatedState(task)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit(currentItem)
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onComplete(currentItem)
                    Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show()
                }
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        // positional threshold of 25%
        positionalThreshold = { it * .25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = { DismissBackground(dismissState)},
        content = {
            TaskCard(task)
        }
    )
}