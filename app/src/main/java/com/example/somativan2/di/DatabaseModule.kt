package com.example.somativan2.di

import android.content.Context
import androidx.room.Room
import com.example.somativan2.data.local.AppDatabase
import com.example.somativan2.data.local.CidadeDao
import com.example.somativan2.data.local.PreferenciaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "clima.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCidadeDao(database: AppDatabase): CidadeDao = database.cidadeDao()

    @Provides
    fun providePreferenciaDao(database: AppDatabase): PreferenciaDao = database.preferenciaDao()
}
