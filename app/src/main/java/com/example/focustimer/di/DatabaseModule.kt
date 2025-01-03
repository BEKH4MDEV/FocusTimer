package com.example.focustimer.di

import android.content.Context
import androidx.room.Room
import com.example.focustimer.data.local.AppDatabase
import com.example.focustimer.data.local.dao.TimerSessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "focus_timer"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTimerSessionDao(db: AppDatabase): TimerSessionDao = db.timerSessionDao()
}