package com.trevorwiebe.caldav.presentation.calendarEvents.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun DayOfWeekText(
    dayInitial: String,
    modifier: Modifier
) {
    Text(
        modifier = modifier,
        text = dayInitial,
        textAlign = TextAlign.Center,
        fontSize = 14.sp
    )
}