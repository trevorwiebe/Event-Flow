package com.trevorwiebe.caldav.domain.usecases.discover_cal

import com.trevorwiebe.caldav.data.remote.CalDavApi
import com.trevorwiebe.caldav.domain.model.AuthCalendarModel
import com.trevorwiebe.caldav.domain.parser.AuthCalendarParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAuthCalendars(
    val calDavApi: CalDavApi
) {

    private val authCalendarParser = AuthCalendarParser()

    suspend operator fun invoke(
        username: String, password: String, url: String
    ): Flow<List<AuthCalendarModel>> {
        return calDavApi.getCalendarLinks(username, password, url)
            .map { dataString ->
                val authCalList = authCalendarParser.parseCalendarLinks(dataString)
                val urlToRemove = authCalList[0].calendarUrl
                authCalList.map {
                    val urlToAdd = it.calendarUrl
                    it.copy(
                        username = username,
                        password = password,
                        calendarUrl = url.replace(urlToRemove, urlToAdd)
                    )
                }
            }
    }
}