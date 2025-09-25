package com.humanmusik.cleanhome.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt


fun Color.Companion.fromHex(color: String) = Color(("#$color").toColorInt())