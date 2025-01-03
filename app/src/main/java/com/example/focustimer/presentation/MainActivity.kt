package com.example.focustimer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.focustimer.presentation.home.HomeScreen
import com.example.focustimer.presentation.home.HomeScreenViewModel
import com.example.focustimer.presentation.theme.FocusTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSize = calculateWindowSizeClass(
                activity = this
            )
            FocusTimerTheme(
                windowSize = windowSize.widthSizeClass
            ) {
                Scaffold {
                    HomeScreen(
                        paddingValues = it,
//                         viewModel = homeScreenViewModel
                        )
                }
            }
        }
    }
}