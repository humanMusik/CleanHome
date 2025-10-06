package com.humanmusik.cleanhome.di

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.CleanHomeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
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
}