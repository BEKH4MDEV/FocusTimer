package com.example.focustimer.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.focustimer.R
import com.example.focustimer.domain.model.TimerTypeEnum
import com.example.focustimer.presentation.components.AutoResizedText
import com.example.focustimer.presentation.components.BorderedIcon
import com.example.focustimer.presentation.components.CircleDot
import com.example.focustimer.presentation.components.CustomButton
import com.example.focustimer.presentation.components.InformationItem
import com.example.focustimer.presentation.components.TimerTypeItem
import com.example.focustimer.presentation.theme.FocusTimerTheme

@Composable
fun HomeScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val timeState by viewModel.timerValueState
    val timerTypeState by viewModel.timerTypeState
    val roundsState by viewModel.roundsSate
    val todayTimeState by viewModel.todayTimeState

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getTimerSessionByDate()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(FocusTimerTheme.dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                modifier = Modifier.size(FocusTimerTheme.dimens.iconSizeNormal),
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        AutoResizedText(
            text = "Focus Timer", textStyle = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))

        Row {
            CircleDot()
            Spacer(modifier = Modifier.width(FocusTimerTheme.dimens.spacerNormal))
            CircleDot()
            Spacer(modifier = Modifier.width(FocusTimerTheme.dimens.spacerNormal))
            CircleDot(
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(FocusTimerTheme.dimens.spacerNormal))
            CircleDot(
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))

        TimerSession(
            timer = viewModel.millisToMinutes(timeState),
            onIncreaseTap = {
                viewModel.onIncreaseTime()
            },
            onDecreaseTap = {
                viewModel.onDecreaseTime()
            }
        )

        Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))

        TimerTypeSession(
            type = timerTypeState,
            onTap = {
                viewModel.onUpdateType(it)
            }
        )

        Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomButton(
                text = "Start",
                textColor = MaterialTheme.colorScheme.surface,
                buttonColor = MaterialTheme.colorScheme.primary,
                onTap = {
                    viewModel.onStartTimer()
                }
            )

            CustomButton(
                text = "Reset",
                textColor = MaterialTheme.colorScheme.primary,
                buttonColor = MaterialTheme.colorScheme.surface,
                onTap = {
                    viewModel.onCancelTimer(true)
                }
            )
        }

        Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))

        InformationSession(
            modifier =
                Modifier.weight(1f),
            round = roundsState.toString(),
            time = viewModel.millisToHours(todayTimeState)
        )
    }

}

@Composable
fun TimerSession(
    modifier: Modifier = Modifier,
    timer: String,
    onIncreaseTap: () -> Unit = {},
    onDecreaseTap: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BorderedIcon(
                icon = R.drawable.ic_minus, onTap = onDecreaseTap
            )
            Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))
        }

        AutoResizedText(
            text = timer,
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f),
            textStyle = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BorderedIcon(
                icon = R.drawable.ic_plus, onTap = onIncreaseTap
            )
            Spacer(modifier = Modifier.height(FocusTimerTheme.dimens.spacerMedium))
        }
    }
}

@Composable
fun TimerTypeSession(
    modifier: Modifier = Modifier,
    type: TimerTypeEnum,
    onTap: (TimerTypeEnum) -> Unit = {}
) {

    val gridCount = 3

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .height(FocusTimerTheme.dimens.spacerLarge),
        columns = GridCells.Fixed(gridCount),
        horizontalArrangement = Arrangement.spacedBy(FocusTimerTheme.dimens.paddingNormal),
        verticalArrangement = Arrangement.spacedBy(FocusTimerTheme.dimens.paddingNormal)
    ) {
        items(
            TimerTypeEnum.entries,
            key = { it.title }
        ) {
            TimerTypeItem(
                text = it.title,
                textColor = if (type === it)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary,
                onTap = { onTap(it) }
            )
        }
    }
}

@Composable
fun InformationSession(
    modifier: Modifier = Modifier,
    round: String,
    time: String
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row {

            InformationItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = round,
                label = "rounds"
            )
            Spacer(modifier = Modifier.fillMaxWidth().weight(1f))
            InformationItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = time,
                label = "time"
            )

        }
    }
}