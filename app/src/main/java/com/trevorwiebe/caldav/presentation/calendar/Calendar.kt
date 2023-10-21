package com.trevorwiebe.caldav.presentation.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.trevorwiebe.caldav.presentation.MainActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    viewModel: MainActivityViewModel,
    navToAddCal: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = { IconButton(onClick = navToAddCal
                ) {
                    Icon(Icons.Filled.AddCircle, contentDescription = null)
                }},
                title = {
                    Text(text = "CalDav", fontSize = 25.sp)
                }
            )
        },
    ) {  innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            if(
                viewModel.state.eventList.isEmpty() &&
                viewModel.state.url.isNotEmpty()
            ){
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }
            }
            items(viewModel.state.calList) { calendar ->
                CalendarView(calendar = calendar)
            }
        }
    }
}