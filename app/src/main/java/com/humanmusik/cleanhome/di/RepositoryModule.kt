package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.repository.auth.AuthRepository
import com.humanmusik.cleanhome.data.repository.auth.FirebaseAuthRepository
import com.humanmusik.cleanhome.data.repository.auth.GetUserId
import com.humanmusik.cleanhome.data.repository.cleanhome.CleanHomeRepositoryImpl
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTask
import com.humanmusik.cleanhome.data.repository.cleanhome.CreateTaskLog
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfAllResidents
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfAllRooms
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfEnrichedTaskById
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfEnrichedTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfResidentById
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfRoomById
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTaskLogsByTaskId
import com.humanmusik.cleanhome.data.repository.cleanhome.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.GetAllHomes
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncHomes
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncRooms
import com.humanmusik.cleanhome.data.repository.cleanhome.SyncTasks
import com.humanmusik.cleanhome.data.repository.cleanhome.UpdateTask
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
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
    abstract fun bindAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGetUserId(
        firebaseAuthRepository: FirebaseAuthRepository,
    ): GetUserId

    @Binds
    @Singleton
    abstract fun bindSyncHomes(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): SyncHomes

    @Binds
    @Singleton
    abstract fun bindGetAllHomes(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): GetAllHomes

    @Binds
    @Singleton
    abstract fun bindSyncTasks(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): SyncTasks

    @Binds
    @Singleton
    abstract fun bindSyncRooms(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): SyncRooms

    @Binds
    @Singleton
    abstract fun bindUpdateTask(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): UpdateTask

    @Binds
    @Singleton
    abstract fun bindsCreateTask(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): CreateTask

    @Binds
    @Singleton
    abstract fun bindFlowOfTasks(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfTasks

    @Binds
    @Singleton
    abstract fun bindFlowOfAllResidents(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfAllResidents

    @Binds
    @Singleton
    abstract fun bindTaskEditor(
        taskEditor: TaskEditorImpl,
    ): TaskEditor

    @Binds
    @Singleton
    abstract fun bindFlowOfAllRooms(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfAllRooms

    @Binds
    @Singleton
    abstract fun bindCreateTaskLog(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): CreateTaskLog

    @Binds
    @Singleton
    abstract fun bindFlowOfTaskLogsByTaskId(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfTaskLogsByTaskId

    @Binds
    @Singleton
    abstract fun bindFlowOfResidentById(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfResidentById

    @Binds
    @Singleton
    abstract fun bindFlowOfRoomById(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfRoomById

    @Binds
    @Singleton
    abstract fun bindFlowOfEnrichedTasks(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfEnrichedTasks

    @Binds
    @Singleton
    abstract fun bindFlowOfEnrichedTaskById(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): FlowOfEnrichedTaskById
}
