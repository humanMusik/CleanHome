package com.humanmusik.cleanhome.di

import android.app.Application
import androidx.room.Room
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
    fun provideCleanHomeDatabase(app: Application): CleanHomeDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = CleanHomeDatabase::class.java,
            name = "cleanhomedb.db"
        ).build()
    }
}