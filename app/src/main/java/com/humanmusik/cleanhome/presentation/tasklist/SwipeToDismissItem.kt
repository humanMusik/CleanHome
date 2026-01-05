package com.humanmusik.cleanhome.presentation.tasklist

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SwipeToDismissItem(
    modifier: Modifier = Modifier,
    onSwipeEndToStart: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()
    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {

            val color by animateColorAsState(
                targetValue = when (dismissState.dismissDirection) {
                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                    SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
                    SwipeToDismissBoxValue.EndToStart -> Color.Green
                },
                label = "swipe"
            )

            Card(
                modifier = modifier
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                when (dismissState.dismissDirection) {
                    SwipeToDismissBoxValue.EndToStart -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterEnd,
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(end = 16.dp),
                                imageVector = androidx.compose.material.icons.Icons.Default.Check,
                                contentDescription = "Complete Task"
                            )
                        }
                    }

                    else -> {}
                }

            }
        }
    ) {
        content()
    }

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> {
                onSwipeEndToStart()
                dismissState.snapTo(SwipeToDismissBoxValue.Settled)
            }

            else -> {}
        }
    }
}

