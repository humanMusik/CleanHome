package com.humanmusik.cleanhome.di

import com.humanmusik.cleanhome.navigation.BackStackInstructor
import com.humanmusik.cleanhome.navigation.StandardBackStackInstructor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun providesStandardBackStackInstructor(): BackStackInstructor {
        return StandardBackStackInstructor()
    }
}