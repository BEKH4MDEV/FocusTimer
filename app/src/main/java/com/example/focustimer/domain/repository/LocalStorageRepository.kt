package com.example.focustimer.domain.repository

import com.example.focustimer.domain.model.TimerSessionModel

interface LocalStorageRepository {
    suspend fun saveTimerSession(
        timerSessionModel: TimerSessionModel
    ): Boolean

    suspend fun getTimerSessionByDate(
        date: String
    ): TimerSessionModel
}