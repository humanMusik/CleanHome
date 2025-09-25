package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.repository.CleanHomeRepositoryImpl
import com.humanmusik.cleanhome.data.repository.CreateTask
import com.humanmusik.cleanhome.data.repository.CreateTaskLog
import com.humanmusik.cleanhome.data.repository.DeleteTask
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.data.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.data.repository.FlowOfAllRooms
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTaskById
import com.humanmusik.cleanhome.data.repository.FlowOfEnrichedTasks
import com.humanmusik.cleanhome.data.repository.FlowOfResidentById
import com.humanmusik.cleanhome.data.repository.FlowOfRoomById
import com.humanmusik.cleanhome.data.repository.FlowOfTaskLogsByTaskId
import com.humanmusik.cleanhome.data.repository.FlowOfTasks
import com.humanmusik.cleanhome.data.repository.UpdateTask
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
    abstract fun bindsDeleteTask(
        cleanHomeRepository: CleanHomeRepositoryImpl,
    ): DeleteTask

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
