package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.repository.CleanHomeRepositoryImpl
import com.humanmusik.cleanhome.data.repository.CreateTask
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.data.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.data.repository.FlowOfAllRooms
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
}