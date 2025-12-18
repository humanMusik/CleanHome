package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.network.home.FirestoreHomeApi
import com.humanmusik.cleanhome.data.network.home.HomeApi
import com.humanmusik.cleanhome.data.network.room.FirestoreRoomApi
import com.humanmusik.cleanhome.data.network.room.RoomApi
import com.humanmusik.cleanhome.data.network.task.FirestoreTaskApi
import com.humanmusik.cleanhome.data.network.task.TaskApi
import com.humanmusik.cleanhome.data.network.user.FirestoreUserApi
import com.humanmusik.cleanhome.data.network.user.UserApi
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

    @Binds
    @Singleton
    abstract fun bindsUserApi(
        firestoreUserApi: FirestoreUserApi,
    ): UserApi

    @Binds
    @Singleton
    abstract fun bindsHomeApi(
        firestoreHomeApi: FirestoreHomeApi,
    ): HomeApi
}