package com.trevorwiebe.caldav.presentation.calendarEvents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.caldav.domain.model.AuthCalendarModel
import com.trevorwiebe.caldav.domain.model.CalendarModel
import com.trevorwiebe.caldav.domain.model.EventModel
import com.trevorwiebe.caldav.domain.usecases.ConnectEventToDayUI
import com.trevorwiebe.caldav.domain.usecases.GetCalendar
import com.trevorwiebe.caldav.domain.usecases.GetCalendarStructure
import com.trevorwiebe.caldav.domain.usecases.GetEvents
import com.trevorwiebe.caldav.domain.usecases.auth.CalendarAuthentication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarEventViewModel @Inject constructor(
    private val calendarAuthentication: CalendarAuthentication,
    private val getCalendarStructure: GetCalendarStructure,
    private val getEvents: GetEvents,
    private val getCalendar: GetCalendar,
    private val connectEventToDayUI: ConnectEventToDayUI
): ViewModel() {

    var state by mutableStateOf(CalendarEventState())

    init {
        loadAuthUser()
    }

    fun onEvent(event: CalendarEventUiEvents){
        when (event){
            is CalendarEventUiEvents.ToggleViewState -> {
                toggleIsGridState()
            }
            is CalendarEventUiEvents.ToggleCalendarVisibility -> {
                toggleCalendarVisibility(event.calendarId)
            }
        }
    }

    private fun loadAuthUser(){

        val dayUiStructure = getCalendarStructure()
        val authUserList = calendarAuthentication.getAuthCalendarList()
        state = state.copy(
            authCalendarModelList = authUserList,
            dayUiList = dayUiStructure
        )
        if(authUserList.isEmpty()){
            state = state.copy(isAuthUserListNull = true)
        }else{
            authUserList.forEach {authUser ->
                loadCalendar(authUser, dayUiStructure)
            }
        }
    }

    private fun loadCalendar(authCalendarModel: AuthCalendarModel, dayUiStructure: List<DayUi>){
        viewModelScope.launch {
            getCalendar(
                authCalendarModel.username, authCalendarModel.password, authCalendarModel.calendarUrl
            ).collect{ calendar ->
                if(calendar != null) {
                    state = state.copy(
                        calList = state.calList.toMutableList().apply {
                            add(calendar)
                        }
                    )
                }

                loadEvents(authCalendarModel, dayUiStructure, calendar?.color ?: "#3b3b3b" )
            }
        }
    }

    private fun loadEvents(
        authCalendarModel: AuthCalendarModel,
        dayUiStructure: List<DayUi>,
        eventColor: String
    ){
        viewModelScope.launch{
            getEvents(
                authCalendarModel.username,
                authCalendarModel.password,
                authCalendarModel.calendarUrl,
                eventColor
            ).collect{eventList ->
                state = state.copy(
                    eventList = state.eventList.toMutableList().apply {
                        addAll(eventList.toMutableList())
                    }
                )
                state = state.copy(
                    dayUiList = connectEventToDayUI(state.eventList, dayUiStructure)
                )
            }
        }
    }

    private fun toggleIsGridState(){
        state = state.copy(isGrid = !state.isGrid)
    }

    private fun toggleCalendarVisibility(calendarId: String){
        val calList = state.calList.toMutableList()
        val calToEdit = calList.find { it.url == calendarId }
        if(calToEdit!=null){
            val position = calList.indexOf(calToEdit)
            calToEdit.isVisible = !calToEdit.isVisible
            calList.removeAt(position)
            calList.add(position, calToEdit)
        }
        state = state.copy(calList = calList)
    }

}

data class CalendarEventState(
    val dayUiList: List<DayUi> = listOf(),
    val calList: List<CalendarModel> = listOf(),
    val eventList: List<EventModel> = listOf(),
    var authCalendarModelList: List<AuthCalendarModel> = emptyList(),
    val isAuthUserListNull: Boolean = false,
    val isGrid: Boolean = true
)