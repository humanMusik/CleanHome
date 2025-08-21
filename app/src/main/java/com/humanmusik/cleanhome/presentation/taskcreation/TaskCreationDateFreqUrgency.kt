package com.humanmusik.cleanhome.presentation.taskcreation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

@Composable
fun TaskCreationDateFreqUrgencyScreen() {
    TaskCreationDateFreqUrgencyContent()
}

@Composable
private fun TaskCreationDateFreqUrgencyContent() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(text = "Date")
            val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
            DatePicker(state = state)

            Text(text = "Frequency")

            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("Weekly") },
                        onClick = { /* Handle edit! */ },
                    )
                    DropdownMenuItem(
                        text = { Text("Fortnightly") },
                        onClick = { /* Handle settings! */ },
                    )
                    DropdownMenuItem(
                        text = { Text("Monthly") },
                        onClick = { /* Handle settings! */ },
                    )
                    DropdownMenuItem(
                        text = { Text("Quarterly") },
                        onClick = { /* Handle settings! */ },
                    )
                    DropdownMenuItem(
                        text = { Text("Bi-annually") },
                        onClick = { /* Handle settings! */ },
                    )
                    DropdownMenuItem(
                        text = { Text("Annually") },
                        onClick = { /* Handle settings! */ },
                    )
                }
            }

            Text(text = "Urgency")
            var selectedIndex by remember { mutableStateOf(0) }
            val options = listOf("Non-Urgent", "Urgent")
            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}