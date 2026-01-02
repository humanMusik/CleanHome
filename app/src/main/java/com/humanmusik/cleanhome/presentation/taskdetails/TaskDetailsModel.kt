package com.humanmusik.cleanhome.presentation.taskdetails

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Resident
import com.humanmusik.cleanhome.domain.model.Room
import com.humanmusik.cleanhome.domain.model.task.Frequency
import com.humanmusik.cleanhome.domain.model.task.Task
import com.humanmusik.cleanhome.domain.model.task.Urgency
import com.humanmusik.cleanhome.presentation.FlowState
import kotlinx.parcelize.Parcelize
import java.time.Duration
import java.time.LocalDate
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal const val taskNameFieldKey = "taskNameField"
internal const val roomFieldKey = "roomField"
internal const val dueDateFieldKey = "dueDateFieldKey"
internal const val frequencyFieldKey = "frequencyFieldKey"
internal const val durationFieldKey = "durationFieldKey"
internal const val urgentFieldKey = "urgentFieldKey"

@Parcelize
data class TaskDetailsState(
    val editableFields: FlowState<List<EditableField>>,
    val dialogState: DialogState,
    val rooms: FlowState<List<Room>>,
) : Parcelable

@Parcelize
data class EditableField(
    val key: EditableFieldKey,
    val title: String,
    val value: String,
    val isEdited: Boolean,
) : Parcelable, Comparable<EditableField> {
    override fun compareTo(other: EditableField): Int =
        editableFieldComparator.compare(this, other)
}

@Parcelize
sealed class DialogState : Parcelable {
    data object Hidden : DialogState()

    data class Visible(
        val inputFieldKey: String,
        val title: String,
        val initialValue: String,
    ) : DialogState()
}

@OptIn(ExperimentalContracts::class)
internal fun DialogState.isVisible(): Boolean {
    contract { returns(true) implies (this@isVisible is DialogState.Visible) }
    return this is DialogState.Visible
}

@Parcelize
data class EditableFieldKey(
    val key: String,
    val index: Int,
) : Parcelable

object FieldKeys {
    val taskNameField = EditableFieldKey(taskNameFieldKey, 0)
    val roomField = EditableFieldKey(roomFieldKey, 1)
    val dueDateField = EditableFieldKey(dueDateFieldKey, 2)
    val frequencyField = EditableFieldKey(frequencyFieldKey, 3)
    val durationField = EditableFieldKey(durationFieldKey, 4)
    val urgentField = EditableFieldKey(urgentFieldKey, 5)
}

@Parcelize
data class TaskEditParcelData(
    val id: Task.Id,
    val taskName: String,
    val roomId: Room.Id,
    val scheduledDate: LocalDate,
    val frequency: Frequency,
    val duration: Duration,
    val urgency: Urgency,
    val assigneeId: Resident.Id,
    val lastCompletedDate: LocalDate?,
) : Parcelable

private val editableFieldComparator: Comparator<EditableField> = compareBy({ it.key.index })
