package com.humanmusik.cleanhome

import android.app.Application
import androidx.work.Configuration
import com.humanmusik.cleanhome.di.AppComponent
import com.humanmusik.cleanhome.di.DaggerAppComponent
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CleanHomeApplication: Application()