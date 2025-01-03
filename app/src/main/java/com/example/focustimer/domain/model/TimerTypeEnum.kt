package com.example.focustimer.domain.model

import com.example.focustimer.core.Constants.FOCUS_TIME
import com.example.focustimer.core.Constants.LONG_BREAK_TIME
import com.example.focustimer.core.Constants.ONE_MIN_IN_SEC
import com.example.focustimer.core.Constants.ONE_SEC_IN_MILLIS
import com.example.focustimer.core.Constants.SHORT_BREAK_TIME

enum class TimerTypeEnum(
    val title: String,
    private val time: Long
) {
    FOCUS(title = "Focus Time", time = FOCUS_TIME),
    SHORT_BREAK(title = "Short Break", time = SHORT_BREAK_TIME),
    LONG_BREAK(title = "Long Break", time = LONG_BREAK_TIME);

    fun timeToMillis(): Long {
        return time * ONE_MIN_IN_SEC * ONE_SEC_IN_MILLIS
    }
}