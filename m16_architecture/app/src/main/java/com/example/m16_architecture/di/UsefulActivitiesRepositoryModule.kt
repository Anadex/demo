package com.example.m16_architecture.di

import com.example.m16_architecture.data.UsefulActivitiesRepository
import com.example.m16_architecture.domain.IUsefulActivitiesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UsefulActivitiesRepositoryModule {
    @Binds
    abstract fun bindIUsefulActivitiesRepository(
        usefulActivitiesRepository: UsefulActivitiesRepository
    ): IUsefulActivitiesRepository
}