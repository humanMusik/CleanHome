package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.domain.HasReversibleTasks
import com.humanmusik.cleanhome.domain.HasReversibleTasksImpl
import com.humanmusik.cleanhome.domain.TaskEditor
import com.humanmusik.cleanhome.domain.TaskEditorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TasksModule {
    @Binds
    @Singleton
    abstract fun bindHasReversibleTasks(
        hasReversibleTasksImpl: HasReversibleTasksImpl,
    ): HasReversibleTasks

    @Binds
    @Singleton
    abstract fun bindTaskEditor(
        taskEditor: TaskEditorImpl,
    ): TaskEditor
}