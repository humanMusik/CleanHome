package com.humanmusik.cleanhome.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.homeDatastore: DataStore<Preferences> by preferencesDataStore("home_preferences")