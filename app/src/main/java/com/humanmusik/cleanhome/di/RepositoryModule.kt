package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.repository.CleanHomeRepositoryImpl
import com.humanmusik.cleanhome.domain.repository.CleanHomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCleanHomeRepository(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): CleanHomeRepository

    // TODO: How to register fun interfaces
}