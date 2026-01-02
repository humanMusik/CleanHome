package com.humanmusik.cleanhome.presentation.utils.composables

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AlertDialogState : Parcelable {
    data object Hide : AlertDialogState
    data class Show(val params: AlertDialogParams) : AlertDialogState
}

@Parcelize
data class AlertDialogParams(
    val key: String,
    val dialogText: String,
    val positiveButtonText: String,
    val negativeButtonText: String? = null,
) : Parcelable {
    companion object {
        val somethingWentWrong = AlertDialogParams(
            key = somethingWentWrongKey,
            dialogText = "Something Went Wrong.",
            positiveButtonText = "Try Again",
            negativeButtonText = "Cancel",
        )

        const val somethingWentWrongKey = "something-went-wrong"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    params: AlertDialogParams,
    onPositiveButtonPressed: () -> Unit,
    onNegativeButtonPressed: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onNegativeButtonPressed,
    ) {
        Surface(
            modifier = Modifier
                .defaultMinSize(
                    minWidth = 100.dp,
                    minHeight = 100.dp,
                ),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(params.dialogText)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(), // Bold ?
                        onClick = onPositiveButtonPressed,
                    ) {
                        Text(params.positiveButtonText)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    if (params.negativeButtonText != null) {
                        TextButton(
                            onClick = onNegativeButtonPressed,
                        ) {
                            Text(params.negativeButtonText)
                        }
                    }
                }
            }
        }
    }
}
