package com.humanmusik.cleanhome.presentation.home

import android.os.Parcelable
import com.humanmusik.cleanhome.domain.model.Home
import com.humanmusik.cleanhome.presentation.FlowState
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val homes: FlowState<List<Home>>,
) : Parcelable