package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.util.toDateString

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.room?.name ?: "",
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = task.assigneeId?.name ?: "",
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.scheduledDate?.toDateString("d MMM") ?: "",
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}