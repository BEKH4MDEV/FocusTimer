package com.example.focustimer.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.focustimer.presentation.theme.FocusTimerTheme

@Composable
fun AutoResizedText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge
) {
    var timeTextStyle by remember {
        mutableStateOf(textStyle)
    }

    val fontSizeFactor = 0.95

    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth(),
        softWrap = false,
        style = timeTextStyle,
        onTextLayout = {
            if (it.didOverflowWidth) {
                timeTextStyle = timeTextStyle.copy(
                    fontSize = textStyle.fontSize * fontSizeFactor
                )
            }
        }
    )
}

@Preview(
    name = "AutoResizedTextPreview",
    showBackground = true,
)
@Composable
fun AutoResizedTextPreview() {

    FocusTimerTheme {
        AutoResizedText(
            text = "Focus Timer"
        )
    }

}
