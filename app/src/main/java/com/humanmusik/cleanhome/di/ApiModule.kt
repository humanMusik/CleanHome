package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.api.room.FirestoreRoomApi
import com.humanmusik.cleanhome.data.api.room.RoomApi
import com.humanmusik.cleanhome.data.api.task.FirestoreTaskApi
import com.humanmusik.cleanhome.data.api.task.TaskApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    @Singleton
    abstract fun bindTaskApi(
        firestoreTaskApi: FirestoreTaskApi
    ): TaskApi

    @Binds
    @Singleton
    abstract fun bindsRoomApi(
        firestoreRoomApi: FirestoreRoomApi,
    ): RoomApi
}