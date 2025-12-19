package com.humanmusik.cleanhome.data.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.humanmusik.cleanhome.domain.model.Home
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun interface GetSelectedHomeId {
    suspend fun getSelectedHomeId(): Home.Id

    companion object {
        suspend operator fun GetSelectedHomeId.invoke() = getSelectedHomeId()
    }
}

fun interface SetHomeIdPreference {
    suspend fun setHomeIdPreference(homeId: Home.Id)

    companion object {
        suspend operator fun SetHomeIdPreference.invoke(homeId: Home.Id) =
            setHomeIdPreference(homeId = homeId)
    }
}

class HomePreferenceRepository @Inject constructor(
    private val homePreferenceDatastore: DataStore<Preferences>,
) : GetSelectedHomeId,
    SetHomeIdPreference {
    override suspend fun getSelectedHomeId(): Home.Id {
        val preference = homePreferenceDatastore
            .data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                preference[KEY]
            }
            .firstOrNull() ?: throw IllegalStateException("Unable to determine the selected homeId")

        return Home.Id(preference)
    }

    override suspend fun setHomeIdPreference(homeId: Home.Id) {
        homePreferenceDatastore
            .edit { preference ->
                preference[KEY] = homeId.value
            }
    }

    companion object {
        private val KEY = stringPreferencesKey("home")
    }
}
