package com.humanmusik.cleanhome.presentation.tasklist

sealed class TaskListEvent {
    data object Refresh: TaskListEvent()
    data class EditTask(val taskId: Int): TaskListEvent()
    data class CompleteTask(val taskId: Int): TaskListEvent()
}