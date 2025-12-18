package com.humanmusik.cleanhome.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.humanmusik.cleanhome.data.CleanHomeDatabase
import com.humanmusik.cleanhome.data.repository.auth.CheckUserSession
import com.humanmusik.cleanhome.data.repository.auth.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [AppModule.BindsModule::class])
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
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindCheckUserSession(
            firebaseAuthRepository: FirebaseAuthRepository,
        ): CheckUserSession
    }
}
