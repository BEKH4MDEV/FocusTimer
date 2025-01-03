package com.example.focustimer.presentation.home

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.CountDownTimer
import androidx.compose.runtime.IntState
import androidx.compose.runtime.LongState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focustimer.core.Constants.ONE_HOUR_IN_MIN
import com.example.focustimer.core.Constants.ONE_MIN_IN_MILLIS
import com.example.focustimer.core.Constants.ONE_MIN_IN_SEC
import com.example.focustimer.domain.model.TimerTypeEnum
import kotlinx.coroutines.launch
import com.example.focustimer.core.Constants.ONE_SEC_IN_MILLIS
import com.example.focustimer.domain.model.Resource
import com.example.focustimer.domain.model.TimerSessionModel
import com.example.focustimer.domain.useCase.GetTimerSessionByDateUseCase
import com.example.focustimer.domain.useCase.SaveTimerSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTimerSessionByDateUseCase: GetTimerSessionByDateUseCase,
    private val saveTimerSessionUseCase: SaveTimerSessionUseCase
) : ViewModel() {

    private lateinit var timer: CountDownTimer

    private var isTimerActive: Boolean = false

    private val _timerValue = mutableLongStateOf(TimerTypeEnum.FOCUS.timeToMillis())
    val timerValueState: LongState = _timerValue

    private val _timerTypeState = mutableStateOf(TimerTypeEnum.FOCUS)
    val timerTypeState: State<TimerTypeEnum> = _timerTypeState

    private val _roundsState = mutableIntStateOf(0)
    val roundsSate: IntState = _roundsState

    private val _todayTimeState = mutableLongStateOf(0)
    val todayTimeState: LongState = _todayTimeState

    private var _sessionTimerValue: Long = 0

    fun onStartTimer() {
        viewModelScope.launch {
            if (!isTimerActive) {
                timer = object : CountDownTimer(
                    _timerValue.longValue,
                    ONE_SEC_IN_MILLIS
                ) {
                    override fun onTick(millisUntilFinished: Long) {
                        _timerValue.longValue = millisUntilFinished
                        _todayTimeState.longValue += ONE_SEC_IN_MILLIS
                        _sessionTimerValue += ONE_SEC_IN_MILLIS
                    }

                    override fun onFinish() {
                        onCancelTimer()
                    }
                }

                timer.start().also {
                    if (!isTimerActive) _roundsState.intValue += 1
                    _sessionTimerValue = 0
                    isTimerActive = true
                }
            }

        }
    }

    fun onCancelTimer(reset: Boolean = false) {
        if (::timer.isInitialized) {
            timer.cancel()
            saveTimerSession()
        }

        if (!isTimerActive || reset) {
            _timerValue.longValue = _timerTypeState.value.timeToMillis()
        }

        isTimerActive = false
    }

    private fun onResetTime() {
        if (isTimerActive) {
            onCancelTimer()
            onStartTimer()
        }
    }

    fun onUpdateType(timerType: TimerTypeEnum) {
        _timerTypeState.value = timerType
        onCancelTimer(true)
    }

    fun onIncreaseTime() {
        _timerValue.longValue += ONE_MIN_IN_MILLIS
        onResetTime()
    }

    fun onDecreaseTime() {
        _timerValue.longValue -= ONE_MIN_IN_MILLIS
        onResetTime()
        if (_timerValue.longValue < 0) onCancelTimer()
    }

    fun getTimerSessionByDate() {

        getTimerSessionByDateUseCase(date = getCurrentDate()).onEach {
            if (it is Resource.Success) {
                _roundsState.intValue = it.data?.round ?: 0
                _todayTimeState.longValue = it.data?.value ?: 0
            }
        }.launchIn(viewModelScope)


    }

    private fun saveTimerSession() {

        val session = TimerSessionModel(
            date = getCurrentDate(),
            value = _sessionTimerValue
        )

        saveTimerSessionUseCase(timerSessionModel = session).onEach {
            when (it) {
                is Resource.Success -> {
                    _sessionTimerValue = 0
                }

                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)

    }


    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MMMM-yyyy")
        return formatter.format(currentDate)
    }

    @SuppressLint("DefaultLocale")
    fun millisToMinutes(value: Long): String {
        val totalSeconds = value / ONE_SEC_IN_MILLIS
        val minutes = (totalSeconds / ONE_MIN_IN_SEC).toInt()
        val seconds = (totalSeconds % ONE_MIN_IN_SEC).toInt()
        return String.format("%02d:%02d", minutes, seconds)
    }

    @SuppressLint("DefaultLocale")
    fun millisToHours(value: Long): String {
        val totalSeconds = value / ONE_SEC_IN_MILLIS
        val seconds = (totalSeconds % ONE_MIN_IN_SEC)
        val totalMinutes = (totalSeconds / ONE_MIN_IN_SEC).toInt()
        val hours = (totalMinutes / ONE_HOUR_IN_MIN)
        val minutes = (totalMinutes % ONE_HOUR_IN_MIN)
        return if (totalMinutes <= ONE_HOUR_IN_MIN) {
            String.format("%02dm %02ds", minutes, seconds)
        } else {
            String.format("%02dh %02dm", hours, minutes)
        }
    }

}