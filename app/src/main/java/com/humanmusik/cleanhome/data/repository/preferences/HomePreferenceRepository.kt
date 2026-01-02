package com.humanmusik.cleanhome.data.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.humanmusik.cleanhome.domain.model.Home
import com.humanmusik.cleanhome.workers.SyncDataWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

fun interface GetSelectedHomeId {
    fun getSelectedHomeId(): Flow<Home.Id?>

    companion object {
        operator fun GetSelectedHomeId.invoke() = getSelectedHomeId()
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
    private val workManager: WorkManager,
) : GetSelectedHomeId,
    SetHomeIdPreference {
    override fun getSelectedHomeId(): Flow<Home.Id?> {
        return homePreferenceDatastore
            .data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preference ->
                preference[KEY]?.let { Home.Id(it) }
            }
            .distinctUntilChanged()
    }

    override suspend fun setHomeIdPreference(homeId: Home.Id) {
        homePreferenceDatastore
            .edit { preference ->
                preference[KEY] = homeId.value
            }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncDataWorker>()
            .setConstraints(constraints).build()

        workManager.enqueue(syncRequest)
    }

    companion object {
        private val KEY = stringPreferencesKey("home")
    }
}
