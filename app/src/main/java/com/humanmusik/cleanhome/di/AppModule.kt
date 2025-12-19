package com.humanmusik.cleanhome.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.repository.auth.CheckUserSession
import com.humanmusik.cleanhome.data.repository.auth.FirebaseAuthRepository
import com.humanmusik.cleanhome.data.repository.preferences.GetSelectedHomeId
import com.humanmusik.cleanhome.data.repository.preferences.HomePreferenceRepository
import com.humanmusik.cleanhome.data.repository.preferences.SetHomeIdPreference
import com.humanmusik.cleanhome.preferences.homeDatastore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        AppModule.UserSessionModule::class,
        AppModule.HomePreferencesModule::class,
    ],
)
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCleanHomeDatabase(appContext: Application): CleanHomeDatabase {
        return CleanHomeDatabase.getDbInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class UserSessionModule {
        @Binds
        @Singleton
        abstract fun bindCheckUserSession(
            firebaseAuthRepository: FirebaseAuthRepository,
        ): CheckUserSession
    }

    @Provides
    @Singleton
    fun provideHomePreferenceDatastore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.homeDatastore

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class HomePreferencesModule {
        @Binds
        @Singleton
        abstract fun bindGetSelectedHomeId(
            homePreferenceRepository: HomePreferenceRepository,
        ): GetSelectedHomeId

        @Binds
        @Singleton
        abstract fun bindSetHomeIdPreference(
            homePreferenceRepository: HomePreferenceRepository,
        ): SetHomeIdPreference
    }
}
