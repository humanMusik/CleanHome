package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.data.repository.CleanHomeRepositoryImpl
import com.humanmusik.cleanhome.domain.model.task.TaskEditor
import com.humanmusik.cleanhome.domain.model.task.TaskEditorImpl
import com.humanmusik.cleanhome.domain.repository.CleanHomeRepository
import com.humanmusik.cleanhome.domain.repository.FlowOfAllResidents
import com.humanmusik.cleanhome.domain.repository.FlowOfTasks
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
}