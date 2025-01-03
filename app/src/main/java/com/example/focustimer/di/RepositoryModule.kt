package com.example.focustimer.di

import com.example.focustimer.data.repository.LocalStorageRepositoryImpl
import com.example.focustimer.domain.repository.LocalStorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindLocalStorageRepository(
        localStorageRepositoryImpl: LocalStorageRepositoryImpl
    ): LocalStorageRepository

}