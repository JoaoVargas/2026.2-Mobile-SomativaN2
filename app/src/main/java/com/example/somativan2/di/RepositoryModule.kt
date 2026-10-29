package com.example.somativan2.di

import com.example.somativan2.data.repository.ClimaRepository
import com.example.somativan2.data.repository.ClimaRepositoryImpl
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
    abstract fun bindClimaRepository(impl: ClimaRepositoryImpl): ClimaRepository
}
