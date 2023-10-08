package com.trevorwiebe.caldav.domain.usecases

import com.trevorwiebe.caldav.data.CalDavApi
import kotlinx.coroutines.flow.Flow

class GetCalendars(
    private val calDavApi: CalDavApi
) {

    suspend operator fun invoke(
        username: String, password: String, url: String
    ): Flow<String> {
        return calDavApi.getCalendars(username, password, url)
    }
}